package com.simaple;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public class MyPhotoBean implements Parcelable {

    private int id;
    private String content;

    public MyPhotoBean(){}

    protected MyPhotoBean(Parcel in) {
        id = in.readInt();
        content = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(content);
    }

    public static final Creator<MyPhotoBean> CREATOR = new Creator<MyPhotoBean>() {
        @Override
        public MyPhotoBean createFromParcel(Parcel in) {
            return new MyPhotoBean(in);
        }

        @Override
        public MyPhotoBean[] newArray(int size) {
            return new MyPhotoBean[size];
        }
    };
}
