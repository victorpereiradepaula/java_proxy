package cache;

import java.util.Date;

public final class CachedItem {

    public final byte[] item;
    public Date createdAt;
    public Date lastAccessDate;

    public CachedItem(byte[] item) {
        this.item = item;
        this.createdAt = new Date();
        this.lastAccessDate = new Date();
    }

    public void updateLastAccessDate() {
        this.lastAccessDate = new Date();
    }
}
