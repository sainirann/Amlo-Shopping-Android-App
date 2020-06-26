package com.amlo.shopping.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.amlo.shopping.dao.ProductDao;
import com.amlo.shopping.util.AuthenticatorUtil;
import com.example.amloshoppingapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass for displaying the products when
 * seller clicks on one of the listing he has posted
 */
public class SellerProductsDisplayFragment extends AbstractProductsDisplayFragment {

    public SellerProductsDisplayFragment() {
        super(R.layout.fragment_category_product_display);
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        String userId = AuthenticatorUtil.getUserId(getActivity().getApplication());
        queryWhereCondition = ProductDao.Properties.Seller.like(userId);
        View view = super.onCreateViewInternal(
            inflater,
            container,
            savedInstanceState,
            new FragmentRecyclerClickFactory.SellerFactory(getActivity().getSupportFragmentManager())
        );
        FloatingActionButton addSellerProdButton = view.findViewById(R.id.add_action);
        addSellerProdButton.show();
        addSellerProdButton.setOnClickListener(v -> {
            Fragment fragment = new SellerCreateProductFragment();
            getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        });
        return view;
    }
}
