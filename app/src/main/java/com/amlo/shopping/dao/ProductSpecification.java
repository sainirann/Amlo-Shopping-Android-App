package com.amlo.shopping.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ProductSpecification {

  @Id(autoincrement = true)
  private long id;

  private String key;

  private String value;

  private String productId;

  @Generated(hash = 963099001)
  public ProductSpecification(long id, String key, String value,
          String productId) {
      this.id = id;
      this.key = key;
      this.value = value;
      this.productId = productId;
  }

  @Generated(hash = 2017934110)
  public ProductSpecification() {
  }

  public long getId() {
      return this.id;
  }

  public void setId(long id) {
      this.id = id;
  }

  public String getKey() {
      return this.key;
  }

  public void setKey(String key) {
      this.key = key;
  }

  public String getValue() {
      return this.value;
  }

  public void setValue(String value) {
      this.value = value;
  }

  public String getProductId() {
      return this.productId;
  }

  public void setProductId(String productId) {
      this.productId = productId;
  }
}
