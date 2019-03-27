package cache;

import java.util.HashMap;
import java.util.LinkedList;

public class WebCache implements WebCacheInterface {

    public static WebCache shared = new WebCache();

    private HashMap<String, CachedItem> cache = new HashMap<>();
    private LinkedList<String> cachePerDate = new LinkedList<>();
    private int cacheSizeInBytes = 50000000;
    private int freeSpace = 50000000;

    private WebCache() {}

    public final synchronized Boolean isCached(String key) {
        return cache.containsKey(key);
    }

    public final synchronized byte[] retrieveFor(String key) {
        CachedItem cached = cache.get(key);

        if(cached == null)
            return null;

        updateLastAccessDate(key, cached);

        return cached.item;
    }

    public final synchronized void saveOnCacheFor(String key, byte[] cachable) {

        int size = cachable.length;
        if(size >= freeSpace) {
            removeCachedItemUntil(size);
        }

        cachePerDate.addFirst(key);

        cache.put(key, new CachedItem(cachable));

        this.freeSpace -= size;
    }

    private final synchronized void removeCachedItemUntil(int size) {
        while(this.cacheSizeInBytes >= size && freeSpace >= size) {
            CachedItem removed = cache.remove(cachePerDate.removeLast());
            this.freeSpace += removed.item.length;
        }
    }

    private final synchronized void updateLastAccessDate(String key, CachedItem cancehdItem) {
        cachePerDate.remove(key);
        cachePerDate.addFirst(key);
        cancehdItem.updateLastAccessDate();
    }
}
