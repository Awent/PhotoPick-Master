package com.awen.photo.photopick.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class Photo implements Parcelable {

    private int id;
    private String path;
    private long size;//byte 字节
    private boolean isLongPhoto;//是否是超长图
    private int width; //图片真实宽度
    private int height;//图片真实高度
    private String mimeType;//图片类型：image/webp、image/jpeg、image/png、image/gif
    private long dateAdd;//添加的时间,用作视频跟图片组合后的排序
    private long duration;//视频的时长，毫秒

    public Photo(){}

    public Photo(int id, String path, long size, boolean isLongPhoto, int width, int height, String mimeType, long dateAdd, long duration) {
        this.id = id;
        this.path = path;
        this.size = size;
        this.isLongPhoto = isLongPhoto;
        this.width = width;
        this.height = height;
        this.mimeType = mimeType;
        this.dateAdd = dateAdd;
        this.duration = duration;
    }

    protected Photo(Parcel in) {
        id = in.readInt();
        path = in.readString();
        size = in.readLong();
        isLongPhoto = in.readByte() != 0;
        width = in.readInt();
        height = in.readInt();
        mimeType = in.readString();
        dateAdd = in.readLong();
        duration = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(path);
        dest.writeLong(size);
        dest.writeByte((byte) (isLongPhoto ? 1 : 0));
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(mimeType);
        dest.writeLong(dateAdd);
        dest.writeLong(duration);
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isLongPhoto() {
        return isLongPhoto;
    }

    public void setLongPhoto(boolean longPhoto) {
        isLongPhoto = longPhoto;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(long dateAdd) {
        this.dateAdd = dateAdd;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isVideo(){
        return mimeType.startsWith("video");
    }

    /**
     * 是否是gif图片
     * @return true代表是gif
     */
    public boolean isGif(){
        return mimeType != null && mimeType.equals("image/gif");
    }

    /**
     * 是否是webp图片
     * @return true代表是webp
     */
    public boolean isWebp(){
        return mimeType != null && mimeType.equals("image/webp");
    }
}
