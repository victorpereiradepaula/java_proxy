package response;

import response.ResponseInterface;

public final class Response404 implements ResponseInterface {

	public String buildResponse() {
		String response = "HTTP/1.1 404 File Not Found\r\n" +
						  "Server: REDES-RC-2018/1.0\r\n" + 
						  "Connection: Close\r\n" +
						  "Content-Type: text/html; charset=utf-8\r\n" +
						  "Content-Length: " + "404\r\n\r\n";

		return response;
	}
}