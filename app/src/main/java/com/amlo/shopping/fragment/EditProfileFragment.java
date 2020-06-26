package com.amlo.shopping.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amlo.shopping.App;
import com.amlo.shopping.activity.LoginActivity;
import com.amlo.shopping.dao.DaoSession;
import com.amlo.shopping.dao.User;
import com.amlo.shopping.dao.UserDao;
import com.amlo.shopping.util.AuthenticatorUtil;
import com.example.amloshoppingapp.R;
import com.google.android.gms.common.util.Strings;

import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass for editing user information in profile
 */
public class EditProfileFragment extends Fragment {

    private TextView emailId;
    private EditText userName, phoneNumber, address1, address2, locality, state;
    private String userNameVal, phoneVal, addressVal1, addressVal2, localityVal, stateVal;
    private Button saveButton;
    private DaoSession daoSession;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        emailId = view.findViewById(R.id.emailid);
        emailId.setText(AuthenticatorUtil.getUserId(getActivity().getApplication()));
        saveButton = view.findViewById(R.id.save_btn);
        saveButton.setOnClickListener(v -> {
            setValuesOfUser(view);
            daoSession = ((App) getActivity().getApplication()).getDaoSession();
            if (validateData()) {
                UserDao userDao = daoSession.getUserDao();
                User user = userDao.load(AuthenticatorUtil.getUserId(getActivity().getApplication()));

                if (user != null) {
                    user.setName(userNameVal);
                    user.setAddress(getAddressFromForm());
                    user.setPhone(phoneVal);
                    userDao.update(user);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, new HomeFragment()).commit();
                }
            } else {
                Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                resetValues();
            }

        });
        return view;
    }

    /**
     * Validate data
     * @return
     */
    public boolean validateData() {
        boolean isValid = true;
        if ((userName.getText().toString()).isEmpty()) {
            userName.setError("Please enter name");
            isValid = false;
        } else if (userName.length() > 50) {
            userName.setError("Beyond Limit");
            isValid = false;
        }

        if ((phoneNumber.getText().toString()).isEmpty()) {
            phoneNumber.setError("Please enter phone number!!!");
            isValid = false;
        } else if (!Pattern.compile("[1-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]").matcher(phoneNumber.getText().toString()).matches()) {
            phoneNumber.setError("Invalid Phone Number!!!");
            isValid = false;
        }

        if (addressVal1.isEmpty()) {
            address1.setError("Please enter address");
            isValid = false;
        } else if (addressVal1.length() > 50) {
            address1.setError("Enter valid address");
            isValid = false;
        }

        if (addressVal2.isEmpty()) {
            address2.setError("Please enter address");
            isValid = false;
        } else if (addressVal2.length() > 50) {
            address2.setError("Enter valid address");
            isValid = false;
        }

        if (localityVal.isEmpty()) {
            locality.setError("Please enter address");
            isValid = false;
        } else if (localityVal.length() > 50) {
            locality.setError("Enter valid address");
            isValid = false;
        }

        if (stateVal.isEmpty()) {
            state.setError("Please enter address");
            isValid = false;
        } else if (stateVal.length() > 50) {
            state.setError("Enter valid address");
            isValid = false;
        }

        return isValid;

    }

    /**
     * reset values
     */
    public void resetValues() {
        userName.setText("");
        address1.setText("");
        address2.setText("");
        locality.setText("");
        state.setText("");
        phoneNumber.setText("");
    }

    /**
     * Extract value from form
     * @param view
     */
    public void setValuesOfUser(View view) {
        userName = view.findViewById(R.id.user_name);
        userNameVal = userName.getText().toString();
        phoneNumber = view.findViewById(R.id.phoneid);
        phoneVal = phoneNumber.getText().toString();
        address1 = view.findViewById(R.id.address_of_user_addr1);
        addressVal1 = address1.getText().toString();
        address2 = view.findViewById(R.id.address_of_user_addr2);
        addressVal2 = address2.getText().toString();
        locality = view.findViewById(R.id.address_of_user_locality);
        localityVal = locality.getText().toString();
        state = view.findViewById(R.id.address_of_user_state);
        stateVal = state.getText().toString();
    }

    /**
     * Get address from form
     * @return
     */
    private String getAddressFromForm() {
        StringBuilder sb = new StringBuilder();
        if (!Strings.isEmptyOrWhitespace(addressVal1)) {
            sb.append(addressVal1);
            sb.append(",");
        }

        if (!Strings.isEmptyOrWhitespace(addressVal2)) {
            sb.append(addressVal2);
            sb.append(",");
        }

        if (!Strings.isEmptyOrWhitespace(localityVal)) {
            sb.append(localityVal);
            sb.append(",");
        }

        if (!Strings.isEmptyOrWhitespace(stateVal)) {
            sb.append(stateVal);
        }

        return sb.toString();
    }
}
