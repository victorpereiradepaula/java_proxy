import java.net.*;

import cache.WebCache;

import java.io.*; 

import request.*;

final class MainServer {

	// Check for a better REGEX later??
	static final String urlPattern = "(http:\\/\\/|https:\\/\\/)?[a-z0-9]+([-.]{1}[a-z0-9]+)*.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?";

	public static void main(String args[]) throws Exception {

		System.out.println("Starting proxy server at: 8081");

		// Might throw exception
		ServerSocket welcomeSocket = new ServerSocket(8081);
		InetAddress address = welcomeSocket.getInetAddress();

		System.out.println("Address:" + address.getHostAddress());

		while(true) {
			System.out.println("Waiting for client connection");
			// Might throw exception
			Socket socket = welcomeSocket.accept();

			InetAddress clientSocket = socket.getInetAddress();

			System.out.println("Client address: " + clientSocket.getHostAddress());

			BufferedReader clientIntake = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
			String clientText = clientIntake.readLine();

			System.out.println("Client requested: " + clientText);

			if (WebCache.shared.isCached(clientText)) {
				final DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
				
				outToClient.writeBytes(WebCache.shared.retrieveWebPageFor(clientText));
				
			} else {
				Request request = new Request(clientText);

				Thread requestThread = new Thread(new RequestThread(request, socket));

				System.out.println("Making request...");

				requestThread.start();
			}
		}
	}
}
