package cache;

interface WebCacheInterface {

    byte[] retrieveFor(String key);
    void saveOnCacheFor(String key, byte[] cachable);
}