package com.amlo.shopping.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.amlo.shopping.App;
import com.amlo.shopping.dao.Cart;
import com.amlo.shopping.dao.CartDao;
import com.amlo.shopping.dao.DaoSession;
import com.amlo.shopping.dao.Item;
import com.amlo.shopping.dao.ItemDao;
import com.amlo.shopping.dao.JoinItem;
import com.amlo.shopping.dao.JoinItemDao;
import com.amlo.shopping.dao.Product;
import com.amlo.shopping.dao.ProductDao;
import com.amlo.shopping.dao.ProductSpecification;
import com.amlo.shopping.util.AuthenticatorUtil;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.amloshoppingapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Abstract fragement which shows the product details (used for both buyer and seller view)
 */
public abstract class AbstractProductDetailsFragment extends Fragment {

  public static final String PRODUCT_ID = "productId";

  private String productId;
  private ProductDao productDao;

  private boolean disableButtons;

  public AbstractProductDetailsFragment(boolean disableButtons) {
    this.disableButtons = disableButtons;
  }

  private String formatProductSpecification(List<ProductSpecification> specs) {
    StringBuilder sb = new StringBuilder();
    for (ProductSpecification spec : specs) {
      String s = Optional.ofNullable(spec.getKey())
          .map(sp -> spec.getKey() + "=" + spec.getValue())
          .orElse(spec.getValue());
      sb.append(s);
    }
    return sb.toString();
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_product_details, container, false);

    DaoSession session = ((App)getActivity().getApplication()).getDaoSession();

    productDao = session.getProductDao();

    Product product = productDao.load(productId);

    ImageView imageView = view.findViewById(R.id.product_details_image);

    Glide.with(getContext()).asBitmap().load(product.getImage().split(",")[0])
        .placeholder(R.drawable.not_found).into(imageView);

    TextView name = view.findViewById(R.id.product_details_name);
    name.setText(product.getProductName());

    TextView desc = view.findViewById(R.id.product_details_short_desc);
    desc.setText(product.getDescription());

    TextView detailsInfo = view.findViewById(R.id.product_details_category);
    detailsInfo.setText(product.getCategory());

    ElegantNumberButton elegantNumberButton = view.findViewById(R.id.qty_button_product);

    CartDao cartDao = session.getCartDao();
    Cart cart = cartDao.load(AuthenticatorUtil.getUserId(getActivity().getApplication()));
    List<Item> items = new ArrayList<>();
    if (cart != null) {
      cart.resetItem();
      items = cart.getItem();
      items = items.stream().filter(i -> i.getProductId().equals(productId)).collect(Collectors.toList());
    }
    if (cart != null && !items.isEmpty()) {
      //Load the existing quantity from cart when viewing the product
      elegantNumberButton.setNumber(String.valueOf(items.get(0).getQuantity()));
    }

    Button addToCartButton = view.findViewById(R.id.submit_product);

    if (disableButtons) {
      elegantNumberButton.setVisibility(View.INVISIBLE);
      addToCartButton.setVisibility(View.INVISIBLE);
    } else {
      //Adds to cart database
      addToCartButton.setOnClickListener(v -> {
        insertToCart(session, product, elegantNumberButton);

        Toast.makeText(getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
        Log.i(getClass().getCanonicalName(), "Persisted in cart");
      });
    }

    return view;
  }

  /**
   * Insert to cart when add button is clicked
   * @param session
   * @param product
   * @param elegantNumberButton
   */
  private void insertToCart(DaoSession session, Product product, ElegantNumberButton elegantNumberButton) {
    //insert into cart
    CartDao cartDao = session.getCartDao();
    ItemDao itemDao = session.getItemDao();
    JoinItemDao joinItemDao = session.getJoinItemDao();

    String userId = AuthenticatorUtil.getUserId(getActivity().getApplication());
    Cart cart = cartDao.load(userId);
    if (cart == null) {
      cart = new Cart();
      cart.setId(userId);
      cartDao.insert(cart);
    }

    List<Item> existingItems = cart.getItem().stream().filter(i -> i.getProductId().equals(productId)).collect(Collectors.toList());
    if (!existingItems.isEmpty()) {
      Item item = existingItems.get(0);
      item.setQuantity(Integer.parseInt(elegantNumberButton.getNumber()));
      itemDao.update(item);
    } else {
      Item productItem = new Item();
      productItem.setId(userId + "::" + productId);
      productItem.setProductId(productId);
      productItem.setQuantity(Integer.parseInt(elegantNumberButton.getNumber()));
      productItem.setProduct(product);
      itemDao.insert(productItem);

      JoinItem joinItem = new JoinItem();
      joinItem.setCartOrOrderId(userId);
      joinItem.setItemId(productItem.getId());
      joinItemDao.insert(joinItem);

      cart.resetItem();

      List<Item> items = cart.getItem();
      if (items == null) {
        items = new ArrayList<>();
      }
      items.add(productItem);
      cartDao.update(cart);
    }
  }


  @Override
  public void setArguments(@Nullable Bundle args) {
    if (args != null) {
      this.productId = args.getString(PRODUCT_ID);
    }
    super.setArguments(args);
  }
}
