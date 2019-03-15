package request;

import java.net.URL;
import java.util.HashMap;

final class WebCache implements WebCacheInterface {

	private HashMap<URL, String> cache = new HashMap<URL, String>();

	private WebCache() {}

	public final synchronized Boolean isCahed(URL url) {
		return cache.containsKey(url);
	}

	public final synchronized String retrieveWebPageFor(URL url) {
		if (this.isCahed(url)) {
			return cache.get(url);
		} else {
			return "";
		}
	}

	public final synchronized void saveWebPageFor(URL url, String webPage) {
		cache.put(url, webPage);
	}
}