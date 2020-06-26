package com.amlo.shopping.fragment;

import android.os.Bundle;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.amlo.shopping.App;
import com.amlo.shopping.dao.DaoSession;
import com.amlo.shopping.dao.DbBackedList;
import com.amlo.shopping.dao.Item;
import com.amlo.shopping.dao.Order;
import com.amlo.shopping.dao.Product;
import com.amlo.shopping.util.CalculationUtil;
import com.bumptech.glide.Glide;
import com.example.amloshoppingapp.R;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass for order details
 */
public class OrderDetailsFragment extends Fragment {
    private DaoSession daoSession;
    private String orderId;
    private List<Item> itemList;
    private RecyclerViewAdapter adapter;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Order order = daoSession.getOrderDao().load(orderId);
        order.resetItem();
        itemList = order.getItem();
        updateFragment();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        if (args != null) {
            this.orderId = args.getString(FragmentRecyclerClickFactory.ORDER_ID);
            updateFragment();
        }
        super.setArguments(args);
    }

    void updateFragment() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        daoSession = ((App)getActivity().getApplication()).getDaoSession();

        RecyclerView recyclerView = view.findViewById(R.id.order_details_list);
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    /**
     * Recycler view adapter which shows the product for the order
     */
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        @NonNull
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_search_list_model, parent, false);
            return new RecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
            Item item = itemList.get(position);
            Product product = item.getProduct();
            Glide.with(getContext()).asBitmap().load(product.getImage().split(",")[0])
                .placeholder(R.drawable.not_found).into(holder.productImage);

            holder.productName.setText(product.getProductName());
            DecimalFormat df = new DecimalFormat("0.00");
            holder.productPrice.setText("$" + df.format(product.getRetailPrice()));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        /**
         * View holder
         */
        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView productImage;
            private TextView productName;
            private TextView productPrice;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                productImage = itemView.findViewById(R.id.product_image);
                productName = itemView.findViewById(R.id.product_name);
                productPrice = itemView.findViewById(R.id.product_price);
            }
        }
    }
}
