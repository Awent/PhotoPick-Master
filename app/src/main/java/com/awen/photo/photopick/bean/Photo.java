package com.awen.photo.photopick.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class Photo implements Serializable{

    private int id;
    private String path;

    public Photo(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public Photo() {
    }

    protected Photo(Parcel in) {
        id = in.readInt();
        path = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;

        return id == photo.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
