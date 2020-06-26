package com.amlo.shopping.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.amlo.shopping.dao.Category;
import com.amlo.shopping.util.CategoryUtil;
import com.bumptech.glide.Glide;
import com.example.amloshoppingapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass for showing the home items
 * This is the landing which shows various categories which user can shop
 */
public class HomeFragment extends Fragment {

  private List<Category> categories = new ArrayList<>();

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    categories.clear();
    categories.addAll(CategoryUtil.getCategories(getResources()));

    View view = inflater.inflate(R.layout.fragment_home, container, false);

    FragmentRecyclerClickFactory factory = new FragmentRecyclerClickFactory.CategoryFactory(getActivity().getSupportFragmentManager());

    RecyclerView categoryRecyclerView = view.findViewById(R.id.product_category_recycler);
    //categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    categoryRecyclerView.setAdapter(new RecyclerViewAdapter(factory));

    // Inflate the layout for this fragment
    return view;
  }

  /**
   * Categories to stop from Recycler view adapter
   */
  private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private FragmentRecyclerClickFactory factory;

    RecyclerViewAdapter(FragmentRecyclerClickFactory factory) {
      this.factory = factory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_category, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      final Category category = categories.get(position);
      final String categoryName = category.getName();
      Glide.with(getContext()).asBitmap().load(category.getImageUrl())
          .placeholder(R.drawable.not_found).into(holder.imageView);
      holder.textView.setText(categoryName);
      holder.linearLayout.setOnClickListener(factory.create(categoryName));
      holder.cardView.setOnClickListener(factory.create(categoryName));
    }

    @Override
    public int getItemCount() {
      return categories.size();
    }

    /**
     * View holder
     */
    private class ViewHolder extends RecyclerView.ViewHolder {
      private TextView textView;
      private ImageView imageView;
      private LinearLayout linearLayout;
      private CardView cardView;

      ViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.category_text);
        imageView = itemView.findViewById(R.id.category_img);
        linearLayout = itemView.findViewById(R.id.category_layout_parent);
        cardView = itemView.findViewById(R.id.category_card);
      }
    }
  }
}
