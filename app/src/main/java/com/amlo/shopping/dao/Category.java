package com.amlo.shopping.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {

  private final String name;
  private final String imageUrl;

  public Category(String name, String imageUrl) {
    this.name = name;
    this.imageUrl = imageUrl;
  }

  private Category(Parcel in) {
    this.name = in.readString();
    this.imageUrl = in.readString();
  }

  public String getName() {
      return this.name;
  }

  public String getImageUrl() {
      return this.imageUrl;
  }

  //Parcel creator
  public static final Creator<Category> CREATOR = new Creator<Category>() {
    @Override
    public Category createFromParcel(Parcel in) {
      return new Category(in);
    }

    @Override
    public Category[] newArray(int size) {
      return new Category[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
   dest.writeString(name);
   dest.writeString(imageUrl);
  }

  @Override
  public String toString() {
    return name;
  }
}
