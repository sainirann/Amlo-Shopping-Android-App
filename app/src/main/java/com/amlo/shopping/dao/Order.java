package com.amlo.shopping.dao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Order {

  @Id
  public String id;

  private String deliveryAddress;

  private long orderTime;

  private String userId;

  @ToMany
  @JoinEntity(
      entity = JoinItem.class,
      sourceProperty = "cartOrOrderId",
      targetProperty = "itemId"
  )
  private List<Item> item;

  /** Used to resolve relations */
  @Generated(hash = 2040040024)
  private transient DaoSession daoSession;

  /** Used for active entity operations. */
  @Generated(hash = 949219203)
  private transient OrderDao myDao;

  @Generated(hash = 1508509434)
  public Order(String id, String deliveryAddress, long orderTime, String userId) {
      this.id = id;
      this.deliveryAddress = deliveryAddress;
      this.orderTime = orderTime;
      this.userId = userId;
  }

  @Generated(hash = 1105174599)
  public Order() {
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDeliveryAddress() {
    return this.deliveryAddress;
  }

  public void setDeliveryAddress(String deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }

  /**
   * To-many relationship, resolved on first access (and after reset).
   * Changes to to-many relations are not persisted, make changes to the target entity.
   */
  @Generated(hash = 234798674)
  public List<Item> getItem() {
    if (item == null) {
      final DaoSession daoSession = this.daoSession;
      if (daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      ItemDao targetDao = daoSession.getItemDao();
      List<Item> itemNew = targetDao._queryOrder_Item(id);
      synchronized (this) {
        if (item == null) {
          item = itemNew;
        }
      }
    }
    return item;
  }

  /** Resets a to-many relationship, making the next get call to query for a fresh result. */
  @Generated(hash = 488856272)
  public synchronized void resetItem() {
    item = null;
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

  public long getOrderTime() {
    return this.orderTime;
  }

  public void setOrderTime(long orderTime) {
    this.orderTime = orderTime;
  }

  public String getUserId() {
      return this.userId;
  }

  public void setUserId(String userId) {
      this.userId = userId;
  }

  /** called by internal mechanisms, do not call yourself. */
  @Generated(hash = 965731666)
  public void __setDaoSession(DaoSession daoSession) {
      this.daoSession = daoSession;
      myDao = daoSession != null ? daoSession.getOrderDao() : null;
  }

}

