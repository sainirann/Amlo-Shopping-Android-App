package com.amlo.shopping.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.amlo.shopping.dao.ProductDao;
import com.example.amloshoppingapp.R;


/**
 * A simple {@link Fragment} subclass for buyer view when clicking on a particular category cart
 */
public class CategoryProductsDisplayFragment extends AbstractProductsDisplayFragment {

    private String selectedCategory;

    public CategoryProductsDisplayFragment() {
        super(R.layout.fragment_category_product_display);
    }

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState
    ) {
        return super.onCreateViewInternal(
            inflater,
            container,
            savedInstanceState,
            new FragmentRecyclerClickFactory.BuyerFactory(getActivity().getSupportFragmentManager())
        );
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        if (args != null) {
            selectedCategory = args.getString(FragmentRecyclerClickFactory.SELECTED_CATEGORY);
            queryWhereCondition = ProductDao.Properties.Category.like(selectedCategory);
        }
        super.setArguments(args);
    }
}
