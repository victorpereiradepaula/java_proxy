package response;

import response.ResponseInterface;

public final class Response200 implements ResponseInterface {

	private final String html;

	public Response200(String html) {
		this.html = html;
	}

	public String buildResponse() {
		String response = "HTTP/1.1 200 OK\r\n" +
						  "Server: REDES-RC-2018/1.0\r\n" + 
						  "Connection: Close\r\n" +
						  "Content-Type: text/html;\r\n" +
						  "Content-Length: " + html.length() + "\r\n\r\n" + 
						  html.toString();
		return response;
	}
}
