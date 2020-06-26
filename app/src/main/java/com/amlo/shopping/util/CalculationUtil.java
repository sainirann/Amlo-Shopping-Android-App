package com.amlo.shopping.util;

import com.amlo.shopping.dao.Item;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Util for calculation of dollar amount
 */
public final class CalculationUtil {
  private CalculationUtil() {
  }

  /**
   * Calculate total from all the items
   * @param items
   * @return
   */
  public static float calculateTotal(List<Item> items) {
    List<Float> totalItems = items.stream().map(i -> i.getProduct().getRetailPrice() * i.getQuantity()).collect(
        Collectors.toList());
    return totalItems.stream().reduce(0f, Float::sum);
  }
}
