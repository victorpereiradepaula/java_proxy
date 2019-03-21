import java.net.*;

import cache.WebCache;

import java.io.*; 

import request.*;

final class MainServer {

	public static void main(String args[]) throws Exception {

		int portNumber = 8080;
		int cacheSize = 50;

		if(args.length == 2) {
			portNumber = Integer.parseInt(args[0]);
			cacheSize = Integer.parseInt(args[1]);
			WebCache.shared.setCacheSizeInMB(cacheSize);
		} else {
			System.out.println("Please, run 'Java MainServer <port-number> <cache-size-MB>'");
			System.exit(0);
		}

		System.out.println("Starting proxy server at: " + portNumber + " with cache size: " + cacheSize + "MB");

		// Might throw exception
		ServerSocket welcomeSocket = new ServerSocket(portNumber);
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

			Request request = new Request(clientText);

			Thread requestThread = new Thread(new RequestThread(request, socket));

			System.out.println("Making request...");

			requestThread.start();
		}
	}
}
