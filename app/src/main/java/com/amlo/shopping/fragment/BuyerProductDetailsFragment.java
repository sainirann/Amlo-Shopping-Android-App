package com.amlo.shopping.fragment;


import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass for buyer view of Product details
 */
public class BuyerProductDetailsFragment extends AbstractProductDetailsFragment {

    public BuyerProductDetailsFragment() {
        // Disable The buttons which are used by seller view
        super(false);
    }
}
