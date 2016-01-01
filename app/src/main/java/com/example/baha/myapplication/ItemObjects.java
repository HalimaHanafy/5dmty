package com.example.baha.myapplication;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ItemObjects {
    private int id;
    private String name;
    private Bitmap photo;
    private String placeid;
    private String placename;


    public ItemObjects(String name, Bitmap photo,int id) {//get price
        this.name = name;
        this.photo = photo;
        this.id=id;
    }

    public ItemObjects(int id,String name) {//get price
        this.name = name;
        this.id=id;
    }


//    public ItemObjects(Parcel in) {
//        String[] data = new String[1];
//        in.readStringArray(data);
//        this.name = data[0];
//    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }



}
