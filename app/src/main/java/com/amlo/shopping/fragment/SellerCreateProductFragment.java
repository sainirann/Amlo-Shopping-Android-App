package com.amlo.shopping.fragment;

import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amlo.shopping.App;
import com.amlo.shopping.dao.DaoSession;
import com.amlo.shopping.dao.Product;
import com.amlo.shopping.dao.ProductDao;
import com.amlo.shopping.util.AuthenticatorUtil;
import com.amlo.shopping.util.CategoryUtil;
import com.example.amloshoppingapp.R;
import com.google.android.gms.common.util.Strings;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass for seller creating the product
 */
public class SellerCreateProductFragment extends Fragment {

    public SellerCreateProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DaoSession daoSession = ((App)getActivity().getApplication()).getDaoSession();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_create_product, container, false);
        Button sellButton = view.findViewById(R.id.seller_button);
        Spinner spinner = view.findViewById(R.id.seller_spinner_category);
        //TODO
        spinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.category_spinner_dropdown_display, CategoryUtil.getCategories(getResources())));

        sellButton.setOnClickListener(v -> {
            //Add the products
            ProductDao productDao = daoSession.getProductDao();
            Product product = createProductFromSellerForm(view);
            if (product != null) {
                productDao.insertOrReplace(product);
                Toast.makeText(getContext(), "Product Added", Toast.LENGTH_LONG);
                getFragmentManager().popBackStackImmediate();
            }
        });
        return view;
    }


    /**
     * Load product information from the form
     * @param view
     * @return
     */
    private Product createProductFromSellerForm(View view) {
        EditText prodName = view.findViewById(R.id.seller_product_name);

        if (Strings.isEmptyOrWhitespace(prodName.getText().toString())) {
            prodName.setError("Name cannot be empty");
            return null;
        }

        EditText prodImg = view.findViewById(R.id.seller_product_img);
        if (Strings.isEmptyOrWhitespace(prodImg.getText().toString())) {
            prodImg.setError("Image URL cannot be empty");
            return null;
        }
        EditText prodPrice = view.findViewById(R.id.seller_product_price);
        if (Strings.isEmptyOrWhitespace(prodPrice.getText().toString())) {
            prodPrice.setError("ProdPrice cannot be empty");
            return null;
        } else {
            try {
                Float.parseFloat(prodPrice.getText().toString());
            } catch (NumberFormatException e) {
                //TODO make sure its 2 digits
                prodPrice.setError("Should be a valid dollar amount");
                return null;
            }
        }
        EditText prodDesc = view.findViewById(R.id.seller_product_desc);
        if (Strings.isEmptyOrWhitespace(prodDesc.getText().toString())) {
            prodDesc.setError("Name cannot be empty");
            return null;
        }

        Spinner spinner = view.findViewById(R.id.seller_spinner_category);

        String category = spinner.getSelectedItem().toString();


        Product product = new Product();
        product.setCategory(category);
        product.setProductName(prodName.getText().toString());
        product.setDescription(prodDesc.getText().toString());
        product.setImage(prodImg.getText().toString());
        product.setUniqId(UUID.randomUUID().toString());
        product.setPid(UUID.randomUUID().toString());
        product.setRetailPrice(Float.parseFloat(prodPrice.getText().toString()));
        product.setSeller(AuthenticatorUtil.getUserId(getActivity().getApplication()));
        return product;
    }

}
