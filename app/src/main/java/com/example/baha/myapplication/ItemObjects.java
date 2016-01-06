package com.example.baha.myapplication;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ItemObjects {
    private int id;
    private String name;
    private Bitmap photo;
    private String placename;
    private String details;
    private String other;
    private String mlong;
    private String mlatt;
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public ItemObjects(String name, Bitmap photo,int id) {//get price
        this.name = name;
        this.photo = photo;
        this.id=id;
    }

    public ItemObjects(int id,String name) {//get price
        this.name = name;
        this.id=id;
    }



    public ItemObjects(int id, String name, String url, String placename, String details, String other, String mlong, String mlatt) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.placename = placename;
        this.details = details;
        this.other = other;
        this.mlong = mlong;
        this.mlatt = mlatt;
    }

    public String getPlacename() {
        return placename;
    }

    public String getDetails() {
        return details;
    }

    public String getOther() {
        return other;
    }

    public String getMlong() {
        return mlong;
    }

    public String getMlatt() {
        return mlatt;
    }

    public void setPlacename(String placename) {
        this.placename = placename;

    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public void setMlong(String mlong) {
        this.mlong = mlong;
    }

    public void setMlatt(String mlatt) {
        this.mlatt = mlatt;
    }

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
