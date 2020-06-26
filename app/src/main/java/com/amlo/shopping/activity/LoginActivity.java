package com.amlo.shopping.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.amlo.shopping.App;
import com.amlo.shopping.dao.DaoSession;
import com.amlo.shopping.dao.User;
import com.amlo.shopping.dao.UserDao;
import com.amlo.shopping.util.AuthenticatorUtil;
import com.example.amloshoppingapp.R;

import io.paperdb.Paper;

/**
 * Login Activity
 */
public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;

    private String nameVal = "";
    private String passwordVal = "";
    private DaoSession daoSession;


    public static final String IS_SIGNUP_CONTEXT = "isUserSignedUp";
    public static final String USER_NAME = "Username";
    public static final String PASSWORD = "Pwd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        Button signInPage = findViewById(R.id.submit);
        Intent i = getIntent();
        if (i.hasExtra(IS_SIGNUP_CONTEXT)) {
            String indicator = i.getStringExtra(IS_SIGNUP_CONTEXT);
            if (indicator != null && indicator.equals("Yes")) {
                userName.setText(i.getStringExtra(USER_NAME));
                password.setText(i.getStringExtra(PASSWORD));
            }
        }
        daoSession = ((App) getApplication()).getDaoSession();
        signInPage.setOnClickListener(v -> openSignIn());
    }

    /**
     *  For the existing user, the credentials would be cross verified with database
     *     and if the user is the authenticated person, it will redirect to Welcome page
     *      If the user is not an authenticated person, they will pop up an error message
     */
    private void openSignIn() {
        if (validateData()) {
            UserDao userDao = daoSession.getUserDao();
            User user = userDao.load(userName.getText().toString());
            if (user != null && user.getPasswordHash().equals(AuthenticatorUtil.generateHash(password.getText().toString()))) {
                Paper.init(this);
                Paper.book().write(USER_NAME, user.getEmailId());
                Paper.book().write(PASSWORD, passwordVal);
                SharedPreferences sharedPreferences = getApplication().getSharedPreferences(
                    AuthenticatorUtil.USER_SHARED_PREF,
                    MODE_PRIVATE
                );
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(AuthenticatorUtil.USER_KEY, user.getEmailId());
                editor.apply();
                Intent intent = new Intent(this, LandingPageActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Incorrect Username or Password", Toast.LENGTH_LONG).show();
                password.setText("");
            }
        }
    }

    /**
     * Validate the username and password data
     * @return
     */
    private boolean validateData() {
        nameVal = userName.getText().toString();
        passwordVal = password.getText().toString();
        return AuthenticatorUtil.validateAuthDetails(nameVal, userName, passwordVal, password);
    }

    /**
     * Password visibility toggle
     * @param view
     */
    public void passwordVisibility(View view) {
        ImageView visibility = findViewById(view.getId());
        if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            visibility.setImageResource(R.drawable.ic_visibility_black_24dp);
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            visibility.setImageResource(R.drawable.ic_visibility_off_black_24dp);
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}
