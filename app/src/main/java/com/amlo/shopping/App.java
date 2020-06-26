package com.amlo.shopping;

import android.app.Application;
import com.amlo.shopping.dao.CartDao;
import com.amlo.shopping.dao.DaoMaster;
import com.amlo.shopping.dao.DaoSession;
import com.amlo.shopping.dao.ItemDao;
import com.amlo.shopping.dao.JoinItem;
import com.amlo.shopping.dao.JoinItemDao;
import com.amlo.shopping.dao.ProductDao;
import com.amlo.shopping.dao.User;
import com.amlo.shopping.dao.UserDao;
import com.amlo.shopping.util.AuthenticatorUtil;
import org.greenrobot.greendao.database.Database;

/**
 * Extension of {@link Application} to initialize the Database information
 */
public class App extends Application {
  private static final String ADMIN_USER = "admin";

  private static final String ADMIN_USER_EMAIL = "admin@amloo.com";

  private DaoSession daoSession;

  @Override
  public void onCreate() {
    super.onCreate();

    // regular SQLite database
    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "amloo-db");
    Database db = helper.getWritableDb();

    // encrypted SQLCipher database
    // note: you need to add SQLCipher to your dependencies, check the build.gradle file
    // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
    // Database db = helper.getEncryptedWritableDb("encryption-key");

    daoSession = new DaoMaster(db).newSession();

    //Create tables if not exists
    UserDao.createTable(db, true);
    ProductDao.createTable(db, true);
    CartDao.createTable(db, true);
    ItemDao.createTable(db, true);
    JoinItemDao.createTable(db, true);

    //Seed an admin user
    UserDao userDao = daoSession.getUserDao();
    User adminUser = new User();
    adminUser.setAddress("");
    adminUser.setDob("");
    adminUser.setEmailId(ADMIN_USER_EMAIL);
    adminUser.setName(ADMIN_USER );
    adminUser.setPasswordHash(AuthenticatorUtil.generateHash(ADMIN_USER_EMAIL));
    adminUser.setPhone("1234567891");
    userDao.insertOrReplace(adminUser);
  }

  public DaoSession getDaoSession() {
    return daoSession;
  }
}
