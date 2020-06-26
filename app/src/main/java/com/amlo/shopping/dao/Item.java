package com.amlo.shopping.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class Item {

  @Id
  private String id;

  private String productId;

  @ToOne(joinProperty = "productId")
  private Product product;

  @NotNull
  private int quantity = 0;

  /** Used to resolve relations */
  @Generated(hash = 2040040024)
  private transient DaoSession daoSession;

  /** Used for active entity operations. */
  @Generated(hash = 182764869)
  private transient ItemDao myDao;

  @Generated(hash = 746020874)
public Item(String id, String productId, int quantity) {
    this.id = id;
    this.productId = productId;
    this.quantity = quantity;
}

@Generated(hash = 1470900980)
  public Item() {
  }

  public String getId() {
      return this.id;
  }

  public void setId(String id) {
      this.id = id;
  }

  public int getQuantity() {
      return this.quantity;
  }

  public void setQuantity(int quantity) {
      this.quantity = quantity;
  }

  @Generated(hash = 1996061979)
private transient String product__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 1737183571)
public Product getProduct() {
    String __key = this.productId;
    if (product__resolvedKey == null || product__resolvedKey != __key) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        ProductDao targetDao = daoSession.getProductDao();
        Product productNew = targetDao.load(__key);
        synchronized (this) {
            product = productNew;
            product__resolvedKey = __key;
        }
    }
    return product;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1681771949)
public void setProduct(Product product) {
    synchronized (this) {
        this.product = product;
        productId = product == null ? null : product.getUniqId();
        product__resolvedKey = productId;
    }
}

/**
   * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
   * Entity must attached to an entity context.
   */
  @Generated(hash = 128553479)
  public void delete() {
      if (myDao == null) {
          throw new DaoException("Entity is detached from DAO context");
      }
      myDao.delete(this);
  }

  /**
   * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
   * Entity must attached to an entity context.
   */
  @Generated(hash = 1942392019)
  public void refresh() {
      if (myDao == null) {
          throw new DaoException("Entity is detached from DAO context");
      }
      myDao.refresh(this);
  }

  /**
   * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
   * Entity must attached to an entity context.
   */
  @Generated(hash = 713229351)
  public void update() {
      if (myDao == null) {
          throw new DaoException("Entity is detached from DAO context");
      }
      myDao.update(this);
  }

  public String getProductId() {
    return this.productId;
}

public void setProductId(String productId) {
    this.productId = productId;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 881068859)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getItemDao() : null;
}
}
