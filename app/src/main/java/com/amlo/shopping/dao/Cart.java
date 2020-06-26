package com.amlo.shopping.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class Cart {

  @Id
  private String id;

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
  @Generated(hash = 1743358803)
  private transient CartDao myDao;

  @Generated(hash = 26595682)
  public Cart(String id) {
      this.id = id;
  }

  @Generated(hash = 1029823171)
  public Cart() {
  }

  public String getId() {
      return this.id;
  }

  public void setId(String id) {
      this.id = id;
  }

  /**
   * To-many relationship, resolved on first access (and after reset).
   * Changes to to-many relations are not persisted, make changes to the target entity.
   */
  @Generated(hash = 1380110937)
  public List<Item> getItem() {
      if (item == null) {
          final DaoSession daoSession = this.daoSession;
          if (daoSession == null) {
              throw new DaoException("Entity is detached from DAO context");
          }
          ItemDao targetDao = daoSession.getItemDao();
          List<Item> itemNew = targetDao._queryCart_Item(id);
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

  /** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1619816777)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getCartDao() : null;
}
  
}

