package com.amlo.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.amlo.shopping.App;
import com.amlo.shopping.dao.Cart;
import com.amlo.shopping.dao.CartDao;
import com.amlo.shopping.dao.DaoSession;
import com.amlo.shopping.dao.Item;
import com.amlo.shopping.dao.ItemDao;
import com.amlo.shopping.dao.Product;
import com.amlo.shopping.util.AuthenticatorUtil;
import com.amlo.shopping.util.CalculationUtil;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.amloshoppingapp.R;

import java.text.DecimalFormat;

/**
 * Cart Activity specifying the items and quantity added to cart for checkout
 */
public class CartActivity extends AppCompatActivity {
    private RecyclerViewAdapter adapter;

    private CartDao cartDao;
    private Cart cart;
    private ItemDao itemDao;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        App app = ((App)getApplication());
        DaoSession session = app.getDaoSession();
        cartDao = session.getCartDao();
        itemDao = session.getItemDao();

        userId = AuthenticatorUtil.getUserId(app);
        cart = cartDao.load(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setId(userId);
            cartDao.insert(cart);
        }

        RecyclerView recyclerView = findViewById(R.id.cart_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        cart.resetItem();
        updateTotal(cart);

        Button placeOrder = findViewById(R.id.proceed_button);
        placeOrder.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderPlacementActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Updates total of the cart from items in the cart
     * @param cart
     */
    private void updateTotal(Cart cart) {
        TextView totalView = findViewById(R.id.total);
        DecimalFormat df = new DecimalFormat("0.00");

        String dollaVal = "$" + df.format(CalculationUtil.calculateTotal(cart.getItem()));
        totalView.setText(dollaVal);
    }

    /**
     * Updates quantity in the database for an item in the cart and updates total accordingly
     * @param item item
     * @param oldQ old quantity
     * @param newQ new quantity
     * @param position Position in the recycler view
     */
    private void updateItemWithQuantity(Item item, int oldQ, int newQ, int position) {
        if (oldQ > 0 && newQ == 0) {
            itemDao.delete(item);
            adapter.notifyItemRemoved(position);
        } else {
            item.setQuantity(newQ);
            itemDao.update(item);
        }
        cart.resetItem();
        adapter.notifyDataSetChanged();
        updateTotal(cart);
    }

    /**
     * Adapter for recycler view
     */
    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_product_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@ NonNull ViewHolder holder, int position) {
            Item item = cart.getItem().get(position);
            Product product = item.getProduct();

            Glide.with(getApplicationContext()).asBitmap().load(product.getImage().split(",")[0])
                .placeholder(R.drawable.not_found).into(holder.image);

            holder.productName.setText(product.getProductName());
            DecimalFormat df = new DecimalFormat("0.00");
            holder.productPrice.setText("$" + df.format(product.getRetailPrice()));
            //holder.productDesc.setText(product.getDescription());
            holder.qty.setNumber(String.valueOf(item.getQuantity()));
            holder.qty.setOnValueChangeListener((view, oldValue, newValue) -> updateItemWithQuantity(item, oldValue, newValue, position));
        }

        @Override
        public int getItemCount() {
            return cart.getItem().size();
        }

        /**
         * View holder for holding the recycler view items
         */
        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView image;
            private TextView productName;
            private TextView productDesc;
            private TextView productPrice;
            private ElegantNumberButton qty;

            ViewHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.display_img);
                productName = itemView.findViewById(R.id.product_name_cart_view);
                productDesc = itemView.findViewById(R.id.product_desc_cart_view);
                productPrice = itemView.findViewById(R.id.product_price_cart_view);
                qty = itemView.findViewById(R.id.qty_button);
            }
        }
    }
}
