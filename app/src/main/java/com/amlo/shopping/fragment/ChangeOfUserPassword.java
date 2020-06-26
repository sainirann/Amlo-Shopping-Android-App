package com.amlo.shopping.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amlo.shopping.App;
import com.amlo.shopping.MainActivity;
import com.amlo.shopping.dao.DaoSession;
import com.amlo.shopping.dao.User;
import com.amlo.shopping.dao.UserDao;
import com.amlo.shopping.util.AuthenticatorUtil;
import com.example.amloshoppingapp.R;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass. for change of password
 */
public class ChangeOfUserPassword extends Fragment {

    private EditText userID, oldPassword, newPassword, retypePassword;
    private String userIDVal, oldPasswordVal, newPasswordVal, retypePasswordVal;
    private Button saveButton;
    private DaoSession daoSession;
    private DrawerLayout drawerLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_of_user_password, container, false);
        saveButton = view.findViewById(R.id.change_password_botton);
        daoSession = ((App)getActivity().getApplication()).getDaoSession();
        saveButton.setOnClickListener(v -> {setValuesOfUser(view);
            if (validateData()) {
                UserDao userDao = daoSession.getUserDao();
                User user = userDao.load(userIDVal);

                if (user != null) {
                    user.setPasswordHash(AuthenticatorUtil.generateHash(newPasswordVal));
                    userDao.update(user);
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
                    resetValues();
                    Toast.makeText(getActivity(), "Invalid UserID", Toast.LENGTH_SHORT);
                }
            }});
        Paper.book().destroy();
        return view;
    }

    public boolean validateData() {
        boolean isValid = true;
        if ((userID.getText().toString()).isEmpty()) {
            userID.setError("Please enter email id!!!");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userID.getText().toString()).matches()) {
            userID.setError("Invalid Email ID");
            isValid = false;
        }
        if (oldPasswordVal.isEmpty()) {
            oldPassword.setError("Please enter password!!!");
            isValid = false;
        } else if (oldPasswordVal.length() < 6 || oldPasswordVal.length() > 32) {
            oldPassword.setError("Password length should be between 6 and 32");
            isValid = false;
        }
        if (newPasswordVal.isEmpty()) {
            newPassword.setError("Please enter password!!!");
            isValid = false;
        } else if (newPasswordVal.length() < 6 || newPasswordVal.length() > 32) {
            newPassword.setError("Password length should be between 6 and 32");
            isValid = false;
        }

        if (retypePasswordVal.isEmpty()) {
            retypePassword.setError("Please enter password!!!");
            isValid = false;
        } else if (retypePasswordVal.length() < 6 || retypePasswordVal.length() > 32) {
            retypePassword.setError("Password length should be between 6 and 32");
            isValid = false;
        }

        if (!newPasswordVal.isEmpty() && !retypePasswordVal.isEmpty() && !newPasswordVal.equals(retypePasswordVal)) {
            retypePassword.setError("Password Mismatch");
            isValid = false;
        }

        if (!newPasswordVal.isEmpty() && !oldPasswordVal.isEmpty() && newPasswordVal.equals(oldPasswordVal)) {
            newPassword.setError("Try with different password");
            isValid = false;
        }
        return isValid;

    }

    public void setValuesOfUser(View view) {

        userID = view.findViewById(R.id.user_id);
        oldPassword = view.findViewById(R.id.old_password);
        newPassword = view.findViewById(R.id.new_password);
        retypePassword = view.findViewById(R.id.retype_new_password);

        oldPasswordVal = oldPassword.getText().toString();
        newPasswordVal = newPassword.getText().toString();
        retypePasswordVal = retypePassword.getText().toString();
        userIDVal = userID.getText().toString();
    }

    public void resetValues() {
        userID.setText("");
        oldPassword.setText("");
        retypePassword.setText("");
        newPassword.setText("");
    }
}
