package request;

import java.net.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;

import response.ResponseInterface;
import response.Response;
import response.ImageResponse;

import cache.*;

public final class RequestThread implements Runnable {

    private final Request request;
    private final Socket socket;

    public RequestThread(Request request, Socket socket) {
        this.request = request;
        this.socket = socket;
    }

    // MARK: - Refactor to handlers
    private void requestImage(String imageUrlString) {

        byte[] cachedImageBytes = WebCache.shared.retrieveFor(imageUrlString);

        if(cachedImageBytes != null) {

            System.out.println("IMAGE IS CACHED");

            InputStream imageBytesInput = new ByteArrayInputStream(cachedImageBytes);
            try {
                BufferedImage cachedimage = ImageIO.read(imageBytesInput);

                if(cachedimage == null)
                    throw new RuntimeException("Failed to convert to BufferedImage");


                final ImageResponse imageResponse = new ImageResponse();
                final DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
                outToClient.writeBytes(imageResponse.buildResponse());

                boolean success = ImageIO.write(cachedimage, "png", socket.getOutputStream());
                System.out.println("IMAGE SENT: " + success );
            } catch(IOException ioException) {

                String errorResponse = "HTTP/1.1 404 File not found\r\n" +
                        "Server: Proxy Server/1.0\r\n" +
                        "Connection: Close\r\n\r\n";

                try {
                    final DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
                    outToClient.writeBytes(errorResponse);
                } catch(Exception exception) {
                    // MARK: - Cant do much about it.
                    exception.printStackTrace();
                }
            }
        } else {
            try {
                URL imageURL = new URL(imageUrlString);

                BufferedImage bufferedImage = ImageIO.read(imageURL);

                if(bufferedImage == null)
                    throw new IOException("Failed to download image");

                final ImageResponse imageResponse = new ImageResponse();
                final DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
                outToClient.writeBytes(imageResponse.buildResponse());

                int lastIndexOfDot = imageUrlString.lastIndexOf(".");
                String extension = imageUrlString.substring(lastIndexOfDot+1, imageUrlString.length());

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, extension, byteArrayOutputStream);

                WebCache.shared.saveOnCacheFor(imageUrlString, byteArrayOutputStream.toByteArray());

                boolean success = ImageIO.write(bufferedImage, extension, socket.getOutputStream());
                System.out.println("IMAGE SENT: " + success );
            } catch(IOException ioException) {
                String errorResponse = "HTTP/1.1 404 File not found\r\n" +
                        "Server: Proxy Server/1.0\r\n" +
                        "Connection: Close\r\n\r\n";

                try {
                    final DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
                    outToClient.writeBytes(errorResponse);
                } catch(Exception exception) {
                    // MARK: - Cant do much about it.
                    exception.printStackTrace();
                }
            }
        }
    }

    // MARK: - Refactor to handlers
    private void plainHTMLRequest(Request request) {

        byte[] cachedItemBytes = WebCache.shared.retrieveFor(request.urlString);

        String cachedHTML = null;
        if(cachedItemBytes != null) {
            cachedHTML = new String(cachedItemBytes, StandardCharsets.UTF_8);
        }

        if(cachedHTML == null) {

            try {
                HttpURLConnection connection = (HttpURLConnection) request.url.openConnection();

                connection.setRequestMethod("GET");
                connection.connect();
                final int responseCode = connection.getResponseCode();

                if(responseCode == 301) {
                    // Redirect
                    String redirectURL = connection.getHeaderField("Location");
                    String cookies = connection.getHeaderField("Set-Cookie");

                    connection = (HttpURLConnection) new URL(redirectURL).openConnection();
                    connection.setRequestProperty("Cookie", cookies);
                    connection.addRequestProperty("Referer", "google.com");
                }

                System.out.println("Response code: " + responseCode);

                final BufferedReader requestResponseBuffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                final StringBuffer content = new StringBuffer();
                String inputLine;
                while((inputLine = requestResponseBuffer.readLine()) != null) {
                    content.append(inputLine);
                }

                requestResponseBuffer.close();
                connection.disconnect();

                final DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());

                final ResponseInterface response = new Response(responseCode, content.toString());

                if(responseCode == 200) {
                    WebCache.shared.saveOnCacheFor(request.urlString, content.toString().getBytes());
                }

                System.out.println(response.buildResponse());
                outToClient.writeBytes(response.buildResponse());

            } catch(IOException exception) {
                exception.printStackTrace();
            }

        } else {
            System.out.println("RESOURCE IS CACHED");

            try {

                final DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
                final ResponseInterface response = new Response(200, cachedHTML);
                System.out.println(response.buildResponse());
                outToClient.writeBytes(response.buildResponse());
            } catch(IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void run() {

        if(request.isImage && request.isValid) {
            requestImage(request.urlString);
        } else if(request.isValid) {
            plainHTMLRequest(request);
        } else {
            System.out.println("Request is invalid");
            System.out.println("Error: " + request.error);
        }

        try {
            socket.close();
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }
}
