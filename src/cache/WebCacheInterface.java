package cache;

public interface WebCacheInterface {

	WebCache shared = new WebCache();
	Boolean isCached(String url);
	String retrieveWebPageFor(String url);
	void saveWebPageFor(String url, String webPage);
}