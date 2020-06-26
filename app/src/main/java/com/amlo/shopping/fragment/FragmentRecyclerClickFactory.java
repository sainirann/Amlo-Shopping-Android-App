package com.amlo.shopping.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.amloshoppingapp.R;

/**
 * A factory interface for creating on click listeners for various recycler views
 */
public interface FragmentRecyclerClickFactory {
  String SELECTED_CATEGORY = "selectedCategory";
  String PRODUCT_ID = "productId";
  String ORDER_ID = "orderId";


  /**
   * Abstract class to hold the fragment manager
   */
  abstract class AbstractFragmentRecyclerClickFactory implements FragmentRecyclerClickFactory {
    FragmentManager fragmentManager;

    AbstractFragmentRecyclerClickFactory(FragmentManager fragmentManager) {
      this.fragmentManager = fragmentManager;
    }
  }

  /**
   * All subclasses has to implement this to determine how click listener should behave
   * @param args
   * @return
   */
  View.OnClickListener create(Object... args);

  /**
   * OnClickListerner Factory when clicking on a card from landing page
   */
  class CategoryFactory extends AbstractFragmentRecyclerClickFactory {
    public CategoryFactory(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    @Override
    public View.OnClickListener create(Object... args) {
      return v -> {
        String selectedCategory = (String)args[0];
        Bundle bundle = new Bundle();
        bundle.putString(SELECTED_CATEGORY, selectedCategory);
        CategoryProductsDisplayFragment fragment = new CategoryProductsDisplayFragment();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit();
      };
    }
  }


  /**
   * Abstract Product Details Listener Factory used by both buyer and seller view
   * @param <F>
   */
  abstract class ProductDetailsClickerFactory<F extends Fragment> extends AbstractFragmentRecyclerClickFactory {

    ProductDetailsClickerFactory(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    View.OnClickListener createListener(String productId, F fragment) {
      return v -> {
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_ID, productId);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit();
      };
    }
  }

  /**
   * Buyer view OnClickListerner Factory when clicking on a product from product list
   * Loads the {@link BuyerProductDetailsFragment}
   *
   */
  class BuyerFactory extends ProductDetailsClickerFactory<BuyerProductDetailsFragment> {
    BuyerFactory(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    @Override
    public View.OnClickListener create(Object... args) {
      return super.createListener((String)args[0], new BuyerProductDetailsFragment());
    }

  }

  /**
   * Seller view OnClickListerner Factory when clicking on a product from product list
   * Loads the {@link SellerProductDetailsFragment}
   *
   */
  class SellerFactory extends ProductDetailsClickerFactory<SellerProductDetailsFragment> {
    SellerFactory(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    @Override
    public View.OnClickListener create(Object... args) {
      return super.createListener((String)args[0], new SellerProductDetailsFragment());
    }
  }


  /**
   * Load products for an order
   */
  class OrderListToProductsFactory extends AbstractFragmentRecyclerClickFactory {
    public OrderListToProductsFactory(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    @Override
    public View.OnClickListener create(Object... args) {
      String orderId = (String)args[0];
      return v -> {
        Fragment fragment = new OrderDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID, orderId);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit();
      };
    }
  }

}
