package com.amlo.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.amlo.shopping.MainActivity;
import com.amlo.shopping.fragment.ChangeOfUserPassword;
import com.amlo.shopping.fragment.EditProfileFragment;
import com.amlo.shopping.fragment.HelpFragment;
import com.amlo.shopping.fragment.HomeFragment;
import com.amlo.shopping.fragment.MyOrdersFragment;
import com.amlo.shopping.fragment.SellerProductsDisplayFragment;
import com.example.amloshoppingapp.R;
import com.google.android.material.navigation.NavigationView;
import io.paperdb.Paper;

/**
 * Landing page after login. Displays different categories
 */
public class LandingPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.navigation_draw_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggleDrawer = new ActionBarDrawerToggle(
            this, drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggleDrawer);
        toggleDrawer.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .addToBackStack(null)
                .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //Loading the corresponding fragment for menu item (on selection from the drawer)
        switch (menuItem.getItemId()) {
            case R.id.home_page:
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .addToBackStack(null)
                    .commit();
                break;
            case R.id.account_details:
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new EditProfileFragment())
                    .addToBackStack(null)
                    .commit();
                break;
            case R.id.change_password:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ChangeOfUserPassword())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.cart:
                startActivity(new Intent(this, CartActivity.class));
                break;
            case R.id.orders:
                MyOrdersFragment myOrdersFragment = new MyOrdersFragment();
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, myOrdersFragment)
                    .addToBackStack(null)
                    .commit();
                break;
            case R.id.about:
                startActivity(new Intent(this, AboutAppActivity.class));
                break;
            case R.id.help:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new HelpFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.seller:
                SellerProductsDisplayFragment sellerProductDetailsFragment = new SellerProductsDisplayFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, sellerProductDetailsFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.logout:
                Paper.book().destroy();
                startActivity(new Intent(this, MainActivity.class));
                break;
            default:
                Log.w(getClass().getCanonicalName(), "Unsupported navigation item -" + menuItem.getItemId());
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
