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
import com.amlo.shopping.dao.Order;
import com.amlo.shopping.dao.OrderDao;
import com.amlo.shopping.dao.Product;
import com.amlo.shopping.util.AuthenticatorUtil;
import com.amlo.shopping.util.CalculationUtil;
import com.bumptech.glide.Glide;
import com.example.amloshoppingapp.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass for previous order placed by user
 */
public class MyOrdersFragment extends Fragment {
    private static final DateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

    private List<Order> orderList;
    private DaoSession daoSession;
    private RecyclerViewAdapter adapter;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //Load orders for the user
        orderList = new DbBackedList<>(
            daoSession.getOrderDao(),
            OrderDao.Properties.UserId.eq(
            AuthenticatorUtil.getUserId(getActivity().getApplication()))
        );
        updateFragment();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        daoSession = ((App)getActivity().getApplication()).getDaoSession();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.my_orders_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //TODO
        adapter = new RecyclerViewAdapter(
            new FragmentRecyclerClickFactory.OrderListToProductsFactory(getActivity().getSupportFragmentManager())
        );
        recyclerView.setAdapter(adapter);
        return view;
    }

    void updateFragment() {
        adapter.notifyDataSetChanged();
    }

    /**
     * Adapter for orders recycler view
     */
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private FragmentRecyclerClickFactory factory;

        RecyclerViewAdapter(FragmentRecyclerClickFactory factory) {
            this.factory = factory;
        }

        @NonNull
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_orders_layout, parent, false);
            return new RecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
            Order order = orderList.get(position);
            holder.orderId.setText(order.getId());
            DecimalFormat df = new DecimalFormat("0.00");
            order.resetItem();
            holder.orderTotal.setText("$" + df.format(CalculationUtil.calculateTotal(order.getItem())));
            holder.orderTime.setText(FORMATTER.format(new Date(order.getOrderTime())));
            holder.layout.setOnClickListener(factory.create(order.getId()));
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }

        /**
         * View holder
         */
        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView orderId;
            private TextView orderTime;
            private TextView orderTotal;
            private LinearLayout layout;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                orderId = itemView.findViewById(R.id.order_id);
                orderTime = itemView.findViewById(R.id.order_time);
                orderTotal = itemView.findViewById(R.id.order_price);
                layout = itemView.findViewById(R.id.order_list_layout);
            }
        }
    }
}
