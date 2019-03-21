package request;

public final class ErrorPageBuilder {

  public static final ErrorPageBuilder shared = new ErrorPageBuilder();

  public static final String getErrorPage(int error, String description) {
    return String.format("<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><meta http-equiv='X-UA-Compatible' content='ie=edge'><title>Error Page</title></head><body><div style='text-align: center'><h1>Ops, não conseguimos acessar essa página</h1><img src='https://32jn1p2jfust2jm6d92xtg5d-wpengine.netdna-ssl.com/wp-content/uploads/2014/10/Leaking-Pipe.jpg' alt='Error image'><p>Experimente sem o proxy, ele ainda esta em sua fase experimental...</p><div style='display: inline-block; width: 45%'><h2 style='font-size: 50px; margin: 0'>%d</h2></div><div style='display: inline-block; width: 45%'><p style='text-align: justify; margin: 0;padding-right: 18%'>%s</p></div></div></body></html>", error, description);
  }
}