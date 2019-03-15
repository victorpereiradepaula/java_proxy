import java.io.*; 
import java.net.*; 
import java.util.Scanner;

final class MainClient {

	public static void main(String args[]) throws Exception {
		final Scanner userInputScanner = new Scanner(System.in);

		// Might throw IOException!!
		final Socket clientSocket = new Socket("localhost", 8081);

		// getOutputStream Might throw IOException!!
		final DataOutputStream outputToServer = new DataOutputStream(clientSocket.getOutputStream());
		final BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 

		System.out.println("Please type a valid URL. (Example: www.disneyworld.disney.go.com, https://naughtydog.com)");
		final String userInput = userInputScanner.nextLine();

		// Might throw IOException!!
		outputToServer.writeBytes(userInput + '\n');

		final String serverResponse = inFromServer.readLine();

		System.out.println("FROM SERVER: " + serverResponse);

		// Might throw IOException!!
		clientSocket.close();
	}
} 