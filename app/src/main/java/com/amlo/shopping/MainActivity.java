package com.amlo.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.amlo.shopping.activity.LoginActivity;
import com.amlo.shopping.activity.RegistrationActivity;
import com.example.amloshoppingapp.R;

/**
 * The main activity
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button joinNowButton = findViewById(R.id.join_now_btn);
        Button loginButton = findViewById(R.id.login_btn);

        joinNowButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegistrationActivity.class)));

        loginButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));
    }
}
