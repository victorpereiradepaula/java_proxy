package response;

import response.ResponseInterface;

public final class Response implements ResponseInterface {

	private final int statusCode;
	private final String html;

	public Response(int statusCode, String html) {
		this.statusCode = statusCode;
		this.html = html;
	}

	public String buildResponse() {
		String response = "";

		switch(statusCode) {
			case 200:
				response += "HTTP/1.1 200 OK\r\n";
			break;

			case 404:
				response += "HTTP/1.1 404 File Not Found\r\n";
			break;				

			default:
				break;
		}

		response += "Server: REDES-RC-2018/1.0\r\n";
		response += "Connection: Close\r\n";
		response += "Content-Type: text/html;\r\n";
		response += "Content-Length: " + html.length() + "\r\n\r\n";
		response += html.toString();

		return response;
	}
}