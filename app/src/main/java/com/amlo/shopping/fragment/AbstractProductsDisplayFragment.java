package com.amlo.shopping.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.amlo.shopping.App;
import com.amlo.shopping.dao.DbBackedList;
import com.amlo.shopping.dao.Product;
import com.bumptech.glide.Glide;
import com.example.amloshoppingapp.R;
import org.greenrobot.greendao.query.WhereCondition;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Display the product list as a fragement (abstract and used by both buyer and seller)
 */
public abstract class AbstractProductsDisplayFragment extends Fragment {
  private RecyclerViewAdapter adapter;

  private List<Product> productList;

  protected WhereCondition queryWhereCondition;

  private int viewId; //R.layout.fragment_category_product_display

  AbstractProductsDisplayFragment(int viewId) {
    this.viewId = viewId;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    //Load product list from db based on the where condition
    productList = new DbBackedList<>(
        ((App)getActivity().getApplication()).getDaoSession().getProductDao(),
        queryWhereCondition
    );
    updateFragment();
    super.onActivityCreated(savedInstanceState);
  }

  public View onCreateViewInternal(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState,
      FragmentRecyclerClickFactory factory
  ) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(viewId, container, false);

    adapter = new RecyclerViewAdapter(factory);
    RecyclerView productsRecyclerView = view.findViewById(R.id.product_search_list);
    productsRecyclerView.setAdapter(adapter);
    productsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    updateFragment();
    return view;
  }

  void updateFragment() {
    adapter.notifyDataSetChanged();
  }

  /**
   * Adapter for recycler view of product list
   */
  protected class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private FragmentRecyclerClickFactory factory;

    RecyclerViewAdapter(FragmentRecyclerClickFactory factory) {
      this.factory = factory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.product_search_list_model, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      Product product = productList.get(position);

      Glide.with(getContext()).asBitmap().load(product.getImage().split(",")[0])
          .placeholder(R.drawable.not_found).into(holder.productImage);

      holder.productName.setText(product.getProductName());
      DecimalFormat df = new DecimalFormat("0.00");
      holder.productPrice.setText("$" + df.format(product.getRetailPrice()));
      //holder.productDesc.setText(product.getDescription());
      holder.itemView.setOnClickListener(factory.create(product.getUniqId()));
    }

    @Override
    public int getItemCount() {
      return productList.size();
    }

    /**
     * View holder
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
      private ImageView productImage;
      private TextView productName;
      //private TextView productDesc;
      private TextView productPrice;

      ViewHolder(@NonNull View itemView) {
        super(itemView);
        productImage = itemView.findViewById(R.id.product_image);
        productName = itemView.findViewById(R.id.product_name);
        //productDesc = itemView.findViewById(R.id.product_short_desc);
        productPrice = itemView.findViewById(R.id.product_price);
      }
    }
  }


}
