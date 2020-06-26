package com.amlo.shopping.dao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * A list model which is backed by database
 * @param <T>
 * @param <D>
 */
public class DbBackedList<T, D extends AbstractDao<T, ?>> extends AbstractList<T> {

  private volatile long count = -1;
  private final List<T> list = new ArrayList<>();
  private final D dao;
  private final WhereCondition whereCondition;

  public DbBackedList(D productDao, WhereCondition whereCondition) {
    this.dao = productDao;
    this.whereCondition = whereCondition;
  }

  @Override
  public T get(int index) {
    if (list.size() <= index && index < count) {
      //load
      int offset = list.size();
      int limit = Math.max(index, Math.min(offset + 10, (int)count));

      QueryBuilder<T> productQueryBuilder = dao
          .queryBuilder()
          .where(whereCondition)
          .offset(offset)
          .limit(limit);
      List<T> result = productQueryBuilder.list();
      list.addAll(result);
    }
    return list.get(index);
  }

  @Override
  public int size() {
    if (count == -1) {
      this.count = 10;
      //TODO: make this dynamic based on count
      QueryBuilder<T> queryBuilder = dao.queryBuilder().where(whereCondition);
      long dbCount = queryBuilder.count();
      this.count = (dbCount > 10)? 10 : dbCount;
    }
    return (int)count;
  }
}
