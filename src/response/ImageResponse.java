package response;

public final class ImageResponse implements ResponseInterface {

    @Override
    public String buildResponse() {
        String response = "";
        response += "HTTP/1.1 200 OK\r\n";
        response += "Server: REDES-RC-2018/1.0\r\n";
        response += "Connection: Close\r\n";
        response += "Content-Type: image/png\r\n\r\n";

        return response;
    }
}