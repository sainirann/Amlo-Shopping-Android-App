package com.amlo.shopping.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinItem {
  @Id(autoincrement = true)
  private Long id;
  private String cartOrOrderId;
  private String itemId;
  @Generated(hash = 1581199027)
  public JoinItem(Long id, String cartOrOrderId, String itemId) {
      this.id = id;
      this.cartOrOrderId = cartOrOrderId;
      this.itemId = itemId;
  }
  @Generated(hash = 1108096942)
  public JoinItem() {
  }
  public Long getId() {
      return this.id;
  }
  public void setId(Long id) {
      this.id = id;
  }
  public String getCartOrOrderId() {
      return this.cartOrOrderId;
  }
  public void setCartOrOrderId(String cartOrOrderId) {
      this.cartOrOrderId = cartOrOrderId;
  }
  public String getItemId() {
      return this.itemId;
  }
  public void setItemId(String itemId) {
      this.itemId = itemId;
  }
}
