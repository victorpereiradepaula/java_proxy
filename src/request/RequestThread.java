package request;

import java.net.*;
import java.io.*; 

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
			if (WebCache.shared.isCached(request.url)) {
				outToClient.writeBytes(WebCache.shared.retrieveWebPageFor(request.url));
			} else {
				try {
					final HttpURLConnection connection = (HttpURLConnection) request.url.openConnection();
	
					connection.setRequestMethod("GET");
						
					final BufferedReader requestResponseBuffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	
					final StringBuffer content = new StringBuffer();
					String inputLine;
					while((inputLine = requestResponseBuffer.readLine()) != null) {
						content.append(inputLine);
					}
	
					requestResponseBuffer.close();
					connection.disconnect();
	
					final DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
					
					String page = content.toString();
					WebCache.shared.saveWebPageFor(request.url, page);
					outToClient.writeBytes(page);
	
				} catch(IOException exception) {
					System.out.println("IOException!!!");
					System.out.println(exception.toString());
				}
			}
		} else {
			System.out.println("Request is invalid");
			System.out.println("Error: " + request.error);
		}

		try {
			socket.close();
		} catch(IOException exception) { }
	}
}
