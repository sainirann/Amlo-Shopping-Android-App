package com.amlo.shopping.activity;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.amlo.shopping.App;
import com.amlo.shopping.dao.DaoSession;
import com.amlo.shopping.dao.Product;
import com.amlo.shopping.dao.ProductDao;
import com.amlo.shopping.util.AuthenticatorUtil;
import com.example.amloshoppingapp.R;
import com.google.android.gms.common.util.Strings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Activity for About Menu.
 */
public class AboutAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        Button loadDataButton = findViewById(R.id.load_data);
        String userId = AuthenticatorUtil.getUserId(getApplication());
        if (!Strings.isEmptyOrWhitespace(userId) && userId.equals("admin@amloo.com")) {
            loadDataButton.setOnClickListener(v -> loadDataIfNotExists());
        } else {
            loadDataButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Load data from asset, which contains a list of products.
     * This dataset is from kaggle on an ecommerce website called Flipkart
     */
    private void loadDataIfNotExists() {
        try {
            if (new LoadDataTask().execute().get()) {
                Toast.makeText(getApplicationContext(), "Successfully loaded data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Cannot load data", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(getClass().getCanonicalName(), "Error load task", e);
        }
    }

    /**
     * Load a single data file
     * @param productDao DAO Object for product to store product
     * @param flipkartDataFile Data file name
     */
    private void loadDataFile(ProductDao productDao, String flipkartDataFile) {
        Log.i(getClass().getCanonicalName(), "Loading data from - " + flipkartDataFile);
        try (InputStream is = getApplicationContext().getAssets().open(flipkartDataFile)) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);
            List<Product> products = new ArrayList<>();
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonObject = jsonArray.getJSONObject(j);
                Product p = createProductFromJsonObject(jsonObject);
                products.add(p);
            }
            products.get(0).setSeller(AuthenticatorUtil.getUserId(getApplication()));
            productDao.insertOrReplaceInTx(products);
        } catch (Exception e) {
            Log.e(getClass().getCanonicalName(), "Error loading data path - " + flipkartDataFile, e);
        }
    }

    /**
     * Parse Json and create a product
     * @param j Json Object
     * @return {@link Product}
     * @throws JSONException
     */
    private Product createProductFromJsonObject(JSONObject j) throws JSONException {
        Product p = new Product();
        p.setPid(j.getString("pid"));
        p.setUniqId(j.getString("uniqId"));
        p.setBrand(j.getString("brand"));
        p.setCategory(j.getString("category"));
        p.setDescription(j.getString("description"));
        p.setProductUrl(j.getString("productUrl"));
        p.setImage(j.getString("image"));
        p.setProductCategoryTree(j.getString("productCategoryTree"));
        //Convert INR to USD
        p.setRetailPrice(Float.parseFloat(j.getString("retailPrice")) / 70);
        p.setProductName(j.getString("productName"));
        return p;
    }

    /**
     * Task for loading data via {@link AsyncTask}
     */
    private class LoadDataTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                DaoSession session = ((App) getApplication()).getDaoSession();
                ProductDao productDao = session.getProductDao();

                String[] flipkartDataFiles = getApplicationContext().getAssets().list("flipkart/");
                if (flipkartDataFiles == null || flipkartDataFiles.length == 0) {
                    return true;
                }
                for (String flipkartDataFile : flipkartDataFiles) {
                    flipkartDataFile = "flipkart/" + flipkartDataFile;
                    loadDataFile(productDao, flipkartDataFile);
                }
                Log.i(getClass().getCanonicalName(), "Loading finished");
                return true;
            } catch (Exception e) {
                Log.e(getClass().getCanonicalName(), "Error loading data", e);
                return false;
            }
        }
    }

}
