package request;

import java.net.*;
import java.io.*;
import java.awt.image.BufferedImage;
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
        try {
            // MARK: - Google's Workaround for https images
//            String newURLString = new StringBuilder(imageUrlString).insert(4, "s").toString();

            URL imageURL = new URL(imageUrlString);

            BufferedImage bufferedImage = ImageIO.read(imageURL);

            if(bufferedImage == null)
                throw new IOException("IMAGE IS NULL");

            final ImageResponse imageResponse = new ImageResponse();
            final DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            outToClient.writeBytes(imageResponse.buildResponse());

            boolean success = ImageIO.write(bufferedImage, "png", socket.getOutputStream());
            System.out.println("IMAGE SENT: " + success );
        } catch(IOException exception) {
            exception.printStackTrace();
            // MARK: - TODO answer with 500
        }
    }

    // MARK: - Refactor to handlers
    private void plainHTMLRequest(Request request) {
        String cachedHTML = WebCache.shared.retrieveWebPageFor(request.urlString);
        if(cachedHTML == null) {

            try {
                HttpURLConnection connection = (HttpURLConnection) request.url.openConnection();

                connection.setRequestMethod("GET");
                connection.connect();
                final int responseCode = connection.getResponseCode();

                if(responseCode == 301) {
                    // redirect
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
                    WebCache.shared.saveWebPageFor(request.urlString, content.toString());
                }

                System.out.println(response.buildResponse());
                outToClient.writeBytes(response.buildResponse());

            } catch(IOException exception) {
                System.out.println("IOException!!!");
                exception.printStackTrace();
            }

        } else {

            try {

                System.out.println("IS CACHED");

                final DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
                final ResponseInterface response = new Response(200, cachedHTML);
                System.out.println(response.buildResponse());
                outToClient.writeBytes(response.buildResponse());
            } catch(IOException exception) {
                System.out.println("IOException!!!");
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
