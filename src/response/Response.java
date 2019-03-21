package response;

public final class Response implements ResponseInterface {

	private final int statusCode;
	private final String html;

	public Response(int statusCode, String html) {
		this.statusCode = statusCode;
		this.html = html;
	}

	public String buildResponse() {
		String response = "";
		String responseHtml = "";

		switch(statusCode) {
			case 200:
				response += "HTTP/1.1 200 OK\r\n";
				responseHtml = html.toString();
			break;

			case 404:
				response += "HTTP/1.1 200 File OK\r\n";
				responseHtml = getErrorPage(statusCode, "File Not Found");
			break;				

			default:
				response += String.format("HTTP/1.1 200 OK\r\n");
				responseHtml = getErrorPage(statusCode, "Error");
				break;
		}

		response += "Server: Proxy Server/1.0\r\n";
		response += "Connection: Close\r\n";
		response += "Content-Type: text/html;\r\n";
		response += "Content-Length: " + responseHtml.length() + "\r\n\r\n";
		response += responseHtml;

		return response;
	}

	private final String getErrorPage(int error, String description) {
    return "<!DOCTYPE html><html lang=\"pt-br\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\"><title>Proxy Error Page</title></head><body><div style=\"text-align: center\"><h1>Ops, algo deu errado...</h1><img src=\"https://32jn1p2jfust2jm6d92xtg5d-wpengine.netdna-ssl.com/wp-content/uploads/2014/10/Leaking-Pipe.jpg\" alt=\"Error image\"><p>Experimente sem o proxy, ele ainda esta em sua fase experimental...</p><div style=\"display: inline-block; width: 45%\"><h2 style=\"font-size: 50px; margin: 0\">" + error + "</h2></div><div style=\"display: inline-block; width: 45%\"><p style=\"text-align: justify; margin: 0;padding-right: 18%\">" + description + "</p></div></div></body></html>";
  }
}