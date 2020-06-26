package com.amlo.shopping.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import com.google.android.gms.common.util.Strings;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Util for authentication
 */
public final class AuthenticatorUtil {
  public static final String USER_SHARED_PREF = "USER";
  public static final String USER_KEY = "USER";

  private AuthenticatorUtil() {
  }

  /**
   * Generate hash for storing in db, rather than real password for security
   * @param password
   * @return
   */
  public static String generateHash(String password)  {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(password.getBytes(StandardCharsets.UTF_8));

      byte[] messageDigest = md.digest();

      // Create Hex String
      StringBuilder sb = new StringBuilder();
      for (byte b : messageDigest) {
        sb.append(String.format("%02x", b&0xff));
      }
      return sb.toString();
    } catch (Exception e) {
      Log.e(AuthenticatorUtil.class.getCanonicalName(),  "Cannot generate hash", e);
    }
    return "";
  }

  /**
   * Validate Authenticationd Details form
   * @param nameVal
   * @param userName
   * @param passwordVal
   * @param password
   * @return
   */
  public static boolean validateAuthDetails(String nameVal, EditText userName, String passwordVal, EditText password) {
    boolean isValid = true;
    if (nameVal.isEmpty()) {
      userName.setError("Please enter username!!!");
      isValid = false;
    }
    if (passwordVal.isEmpty()) {
      password.setError("Please enter password!!!");
      isValid = false;
    } else if (password.length() < 6 || password.length() > 32) {
      password.setError("Password length should be between 6 and 32");
      isValid = false;
    }
    return isValid;
  }

  /**
   * Get Logged in user from shared preferences (used mainly for different application invocations)
   * @param application
   * @return
   */
  public static String getUserId(Application application) {
    SharedPreferences sharedPreferences = application.getSharedPreferences(AuthenticatorUtil.USER_SHARED_PREF, Context.MODE_PRIVATE);
    String userId = sharedPreferences.getString(AuthenticatorUtil.USER_KEY, "");
    if (Strings.isEmptyOrWhitespace(userId)) {
      Log.e("User", "Empty user");
    }
    return userId;
  }

}
