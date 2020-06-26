package com.amlo.shopping.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {
  @Id
  private String emailId;
  @NotNull
  private String name;
  @NotNull
  private String passwordHash;
  @NotNull
  private String address;
  @NotNull
  private String dob;
  @NotNull
  private String phone;

  @Generated(hash = 1193498149)
public User(String emailId, @NotNull String name, @NotNull String passwordHash,
        @NotNull String address, @NotNull String dob, @NotNull String phone) {
    this.emailId = emailId;
    this.name = name;
    this.passwordHash = passwordHash;
    this.address = address;
    this.dob = dob;
    this.phone = phone;
}
@Generated(hash = 586692638)
  public User() {
  }
  public String getEmailId() {
      return this.emailId;
  }
  public void setEmailId(String emailId) {
      this.emailId = emailId;
  }
  public String getName() {
      return this.name;
  }
  public void setName(String name) {
      this.name = name;
  }
  public String getPasswordHash() {
      return this.passwordHash;
  }
  public void setPasswordHash(String passwordHash) {
      this.passwordHash = passwordHash;
  }
  public String getPhone() {
      return this.phone;
  }
  public void setPhone(String phone) {
      this.phone = phone;
  }
  public String getAddress() {
      return address;
  }
  public void setAddress(String address) {
      this.address = address;
  }
public String getDob() {
    return this.dob;
}
public void setDob(String dob) {
    this.dob = dob;
}

}
