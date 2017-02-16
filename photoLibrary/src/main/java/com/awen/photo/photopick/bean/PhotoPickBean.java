package com.awen.photo.photopick.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Awen <Awentljs@gmail.com>.
 */
public class PhotoPickBean implements Parcelable {

    private int maxPickSize;    //最多可以选择多少张图片
    private int pickMode;       //单选还是多选
    private int spanCount;      //recyclerview有多少列
    private boolean showCamera;//是否展示拍照icon
    private boolean clipPhoto; //是否启动裁剪图片
    private boolean originalPicture;//是否选择的是原图

    public PhotoPickBean(){}

    private PhotoPickBean(Parcel in) {
        maxPickSize = in.readInt();
        pickMode = in.readInt();
        spanCount = in.readInt();
        showCamera = in.readByte() != 0;
        clipPhoto = in.readByte() != 0;
        originalPicture = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(maxPickSize);
        dest.writeInt(pickMode);
        dest.writeInt(spanCount);
        dest.writeByte((byte) (showCamera ? 1 : 0));
        dest.writeByte((byte) (clipPhoto ? 1 : 0));
        dest.writeByte((byte) (originalPicture ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoPickBean> CREATOR = new Creator<PhotoPickBean>() {
        @Override
        public PhotoPickBean createFromParcel(Parcel in) {
            return new PhotoPickBean(in);
        }

        @Override
        public PhotoPickBean[] newArray(int size) {
            return new PhotoPickBean[size];
        }
    };

    public int getMaxPickSize() {
        return maxPickSize;
    }

    public void setMaxPickSize(int maxPickSize) {
        this.maxPickSize = maxPickSize;
    }

    public int getPickMode() {
        return pickMode;
    }

    public void setPickMode(int pickMode) {
        this.pickMode = pickMode;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public boolean isClipPhoto() {
        return clipPhoto;
    }

    public void setClipPhoto(boolean clipPhoto) {
        this.clipPhoto = clipPhoto;
    }

    public boolean isOriginalPicture() {
        return originalPicture;
    }

    public void setOriginalPicture(boolean originalPicture) {
        this.originalPicture = originalPicture;
    }
}
