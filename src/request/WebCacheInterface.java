package request;

import java.net.URL;

public interface WebCacheInterface {

	WebCache shared;
	Boolean isCahed(URL url);
	String retrieveWebPageFor(URL url);
	void saveWebPageFor(URL url, String webPage);
}