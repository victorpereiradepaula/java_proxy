package request;

import java.net.*;
import java.io.*;

import response.ResponseInterface;
import response.Response;

import cache.*;

public final class RequestThread implements Runnable {

	private final Request request;
	private final Socket socket;

	public RequestThread(Request request, Socket socket) {
		this.request = request;
		this.socket = socket;
	}

	@Override
	public void run() {

		if(request.isValid) {

			String cachedHTML = WebCache.shared.retrieveWebPageFor(request.urlString);
			if(cachedHTML == null) {

				try {
					final HttpURLConnection connection = (HttpURLConnection) request.url.openConnection();

					connection.setRequestMethod("GET");
					connection.connect();
					final int responseCode = connection.getResponseCode();

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
