package com.amlo.shopping.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amlo.shopping.App;
import com.amlo.shopping.MainActivity;
import com.amlo.shopping.dao.DaoSession;
import com.amlo.shopping.dao.User;
import com.amlo.shopping.dao.UserDao;
import com.amlo.shopping.util.AuthenticatorUtil;

import io.paperdb.Paper;

/**
 * Splash screen activity
 */
public class SplashScreenActivity extends AppCompatActivity {

    public static final String USER_NAME = "Username";
    public static final String PASSWORD = "Pwd";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        String userName = Paper.book().read(USER_NAME);
        String password = Paper.book().read(PASSWORD);
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        User user = userDao.load(userName);
        if (user != null && user.getPasswordHash().equals(AuthenticatorUtil.generateHash(password))) {
            Intent intent = new Intent(this, LandingPageActivity.class);
            startActivity(intent);
        } else {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }
}
