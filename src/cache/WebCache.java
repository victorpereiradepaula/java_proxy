package cache;

import java.util.HashMap;
import java.util.LinkedList;

public final class WebCache implements WebCacheInterface {

	private HashMap<String, CachedItem> cache = new HashMap<String, CachedItem>();
	private LinkedList<String> cachePerDate = new LinkedList<String>();
	private int cacheSizeInBytes = 50000000;
	private int freeSpace = 50000000;

	public final synchronized void setCacheSizeInMB(int valueInMB) {
		this.cacheSizeInBytes = valueInMB * 1000000;
	}

	public final synchronized Boolean isCached(String url) {
		return cache.containsKey(url);
	}

	public final synchronized String retrieveWebPageFor(String url) {
		if (this.isCached(url)) {
			CachedItem cachedItem = cache.get(url);
			this.updateLastAccessDate(url, cachedItem);
			return cachedItem.getItem();
		} else {
			return "";
		}
	}

	public final synchronized void saveWebPageFor(String url, String webPage) {
		int pageSize = webPage.getBytes().length;
		if (pageSize <= this.cacheSizeInBytes) {
			addOnCache(url, webPage, pageSize);
		} else {
			System.out.println("Não foi possível adicionar página à cache.");
		}
	}

	private final synchronized void addOnCache(String url, String webPage, int pageSize) {
		if (pageSize >= this.freeSpace) {
			removeCachedItemUntil(pageSize);
		}
		cachePerDate.addFirst(url);
		cache.put(url, new cache.CachedItem(webPage));
		this.freeSpace -= pageSize;
		System.out.println("Página adicionada à cache com sucesso.");
		System.out.printf("Espaço livre na cache: %d\n", this.freeSpace);
	}

	private final synchronized void removeCachedItemUntil(int pageSize) {
		while (this.cacheSizeInBytes >= pageSize && this.freeSpace >= pageSize) {
			CachedItem removedItem = cache.remove(cachePerDate.removeLast());
			this.freeSpace += removedItem.getSize();
		}
	}

	private final synchronized void updateLastAccessDate(String url, CachedItem cachedItem) {
		cachePerDate.remove(url);
		cachePerDate.addFirst(url);
		cachedItem.updateLastAccessDate();
	}
}