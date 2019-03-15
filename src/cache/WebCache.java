package cache;

import java.util.HashMap;

public final class WebCache implements WebCacheInterface {

	private HashMap<String, String> cache = new HashMap<String, String>();

	public final synchronized Boolean isCached(String url) {
		return cache.containsKey(url);
	}

	public final synchronized String retrieveWebPageFor(String url) {
		if (this.isCached(url)) {
			return cache.get(url);
		} else {
			return "";
		}
	}

	public final synchronized void saveWebPageFor(String url, String webPage) {
		cache.put(url, webPage);
	}
}