package com.amlo.shopping.activity;

import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.amloshoppingapp.R;

import android.os.Bundle;

/**
 * Order Placed Notification Activity
 */
public class PlacedOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order);
        Button continueShopping = findViewById(R.id.continue_shopping);
        continueShopping.setOnClickListener(v -> {
            Intent intent = new Intent(this, LandingPageActivity.class);
            startActivity(intent);
        });
    }
}
