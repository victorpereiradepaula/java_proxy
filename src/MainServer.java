import java.net.*;

import cache.WebCache;

import java.io.*; 

import request.*;

final class MainServer {

	public static void main(String args[]) throws Exception {

		if(args.length > 0) {
			String command = args[0];
			if(command.equals("--help")) {
				System.out.println("run 'Java MainServer <port-number>'' to use a custom port");
				System.out.println("If no port is specified then the server will run on 8080");
				System.exit(0);
			}
		}

		int portNumber = 8080;
		if(args.length > 0) {
			portNumber = Integer.parseInt(args[0]);
		}

		System.out.println("Starting proxy server at: " + portNumber);

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
