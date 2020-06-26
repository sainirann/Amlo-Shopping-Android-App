package com.amlo.shopping.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amlo.shopping.App;
import com.amlo.shopping.dao.DaoSession;
import com.amlo.shopping.dao.User;
import com.amlo.shopping.dao.UserDao;
import com.amlo.shopping.util.AuthenticatorUtil;
import com.example.amloshoppingapp.R;
import com.google.android.gms.common.util.Strings;

import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Sign up Activity
 */
public class RegistrationActivity extends AppCompatActivity {

    EditText name, password, email, phone, retypePassword, address1, address2, locality, state, dob;
    Button btnSubmit;

    String nameVal, passwordVal, emailVal, phoneVal, retypePasswordVal, addressVal1, addressVal2, localityVal, stateVal;

    private DaoSession daoSession;

    public static final String IS_SIGNUP_CONTEXT = "isUserSignedUp";
    public static final String USER_NAME = "Username";
    public static final String PASSWORD = "Pwd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        btnSubmit = findViewById(R.id.signupbutton);
        daoSession = ((App) getApplication()).getDaoSession();
        dob = findViewById(R.id.date_of_birth);
        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        // Date of birth - from calendar picker
        dob.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePickerDialog(year, month, day);
            }
        });

        dob.setOnClickListener(v -> showDatePickerDialog(year, month, day));
        // Submit Button for user information
        btnSubmit.setOnClickListener(v -> {
            setValuesOfUser();
            if (validateData()) {
                User user = new User();
                user.setEmailId(email.getText().toString());
                user.setName(name.getText().toString());
                user.setPhone(phone.getText().toString());
                user.setAddress(getAddressFromForm());
                user.setDob(dob.getText().toString());
                user.setPasswordHash(AuthenticatorUtil.generateHash(password.getText().toString()));

                UserDao userDao = daoSession.getUserDao();
                if (userDao.load(email.getText().toString()) == null) {
                    userDao.insert(user);
                    Intent ind = new Intent(getApplicationContext(), LoginActivity.class);
                    ind.putExtra(USER_NAME, emailVal);
                    ind.putExtra(PASSWORD, passwordVal);
                    ind.putExtra(IS_SIGNUP_CONTEXT, "Yes");
                    startActivity(ind);
                } else {
                    Toast.makeText(getApplicationContext(), "Existing User", Toast.LENGTH_LONG).show();
                    resetValues();
                }
            }
        });
    }

    /**
     * Shows the data picker dialog on clicking or changing focus
     * to Data of birth form field
     * @param year current year
     * @param month current month
     * @param day current day
     */
    private void showDatePickerDialog(int year, int month, int day) {
        DatePickerDialog dialog = new DatePickerDialog(
                RegistrationActivity.this,
                (view, year1, month1, day1) -> {
                    month1 = month1 + 1;
                    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                    if ((currentYear - year1 <= 100) && (currentYear - year1 > 0)) {
                        String date = day1 + "/" + month1 + "/" + year1;
                        dob.setText(date);
                    } else {
                        dob.setError("Invalid Date");
                        dob.setText("");
                    }
                }, year, month, day);
        dialog.show();
    }


    /**
     * The fields in the registration page are validated. All the fields are mandatory.
     * Email ID and phone number should be in appropriate format.
     * Username must be unique and the password should be between 6 to 32 characters.
     */
    public boolean validateData() {
        boolean isValid = AuthenticatorUtil.validateAuthDetails(nameVal, name, passwordVal, password);
        if (retypePasswordVal.isEmpty()) {
            retypePassword.setError("Please enter re-enter password!!!");
            isValid = false;
        } else if (retypePassword.length() < 6 || retypePassword.length() > 32) {
            password.setError("Password length should be between 6 and 32");
            isValid = false;
        } else if (!retypePasswordVal.equals(passwordVal)) {
            retypePassword.setError("Password Mismatch");
        }
        if (emailVal.isEmpty()) {
            email.setError("Please enter email id!!!");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailVal).matches()) {
            email.setError("Invalid Email ID");
            isValid = false;
        }

        if (phoneVal.isEmpty()) {
            phone.setError("Please enter phone number!!!");
            isValid = false;
        } else if (!Pattern.compile("[1-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]").matcher(phoneVal).matches()) {
            phone.setError("Invalid Phone Number!!!");
            isValid = false;
        }

        if (!passwordVal.isEmpty() && !retypePasswordVal.isEmpty() && !passwordVal.equals(retypePasswordVal)) {
            retypePassword.setError("Password Mismatch");
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

        if (dob.getText().toString().isEmpty()) {
            dob.setError("Enter date of birth");
            isValid = false;
        } else if((!Pattern.compile("[0-9]?[0-9]/[0-9]?[0-9]/[0-9][0-9][0-9][0-9]").matcher(dob.getText().toString()).matches())) {
            dob.setError("Date should be in mm/dd/yy format");
            isValid = false;
        }

        return isValid;
    }
    /**
     * Reset all the fields if the user has already registered
     */
    public void resetValues() {
        name.setText("");
        password.setText("");
        retypePassword.setText("");
        password.setText("");
        email.setText("");
        phone.setText("");
        address1.setText("");
        address2.setText("");
        locality.setText("");
        state.setText("");
        dob.setText("");
    }

    /**
     * The values in all the fields are stored in corresponding string member fields
     */
    public void setValuesOfUser() {
        name = findViewById(R.id.usernamesignup);
        nameVal = name.getText().toString();
        password = findViewById(R.id.signuppassword);
        passwordVal = password.getText().toString();
        retypePassword = findViewById(R.id.signuprepassword);
        retypePasswordVal = retypePassword.getText().toString();
        email = findViewById(R.id.emailid);
        emailVal = email.getText().toString();
        phone = findViewById(R.id.phone);
        phoneVal = phone.getText().toString();
        address1 = findViewById(R.id.address_of_user_addr1);
        addressVal1 = address1.getText().toString();
        address2 = findViewById(R.id.address_of_user_addr2);
        addressVal2 = address2.getText().toString();
        locality = findViewById(R.id.address_of_user_locality);
        localityVal = locality.getText().toString();
        state = findViewById(R.id.address_of_user_state);
        stateVal = state.getText().toString();
    }

    /**
     * Get address of the user from form
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
