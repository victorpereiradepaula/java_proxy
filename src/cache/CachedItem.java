package cache;

import java.util.Date;

public final class CachedItem {
  
  private int size;
  private String item;
  private Date createdAt;
  private Date lastAccessDate;

  public CachedItem(String item) {
    this.size = item.getBytes().length;
    this.item = item;
    this.createdAt = new Date();
    this.lastAccessDate = new Date();
  }

  public void updateLastAccessDate() {
    this.lastAccessDate = new Date();
    System.out.println("Access date updated.");
  }

  /**
   * @return the lastAccessDate
   */
  public Date getLastAccessDate() {
    return lastAccessDate;
  }

  /**
   * @return the createdAt
   */
  public Date getCreatedAt() {
    return createdAt;
  }

  /**
   * @return the item
   */
  public String getItem() {
    return item;
  }

  /**
   * @return the size
   */
  public int getSize() {
    return size;
  }
}