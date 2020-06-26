package com.amlo.shopping.util;

import android.content.res.Resources;
import com.amlo.shopping.dao.Category;
import com.example.amloshoppingapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Util for category information
 */
public final class CategoryUtil {
  private CategoryUtil() {
  }

  private static final List<Category> CATEGORIES = new ArrayList<>();

  /**
   * Static list of categories
   * @param resources
   * @return
   */
  public static List<Category> getCategories(Resources resources) {
    if (CATEGORIES.isEmpty()) {
      CATEGORIES.addAll(Arrays.asList(
          new Category(resources.getString(R.string.clothing_fashion_accessories), "https://images.unsplash.com/photo-1527683040093-3a2b80ed1592?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1130&q=80"),
          new Category(resources.getString(R.string.toys_school_supplies), "https://images.unsplash.com/photo-1568301856220-8d0dc08a6d48?ixlib=rb-1.2.1&auto=format&fit=crop&w=975&q=80"),
          new Category(resources.getString(R.string.automotive), "https://st2.depositphotos.com/6644020/11546/i/950/depositphotos_115468952-stock-photo-industrial-spare-parts.jpg"),
          new Category(resources.getString(R.string.furniture_home_supplies), "https://images.unsplash.com/photo-1484154218962-a197022b5858?ixlib=rb-1.2.1&auto=format&fit=crop&w=1653&q=80"),
          new Category(resources.getString(R.string.baby_care), "https://images.unsplash.com/photo-1583007054045-800d870b8249?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=668&q=80"),
          new Category(resources.getString(R.string.electronics), "https://images.unsplash.com/photo-1522273400909-fd1a8f77637e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1900&q=80")
      ));
    }
    return CATEGORIES;
  }

}
