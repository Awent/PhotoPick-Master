package com.awen.photo.photopick.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.awen.photo.photopick.controller.PhotoPickConfig;

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
    private boolean showGif;//是否展示gif图片
    private int mediaType;//默认0，显示所有图片跟视频，1：只显示图片，2：只显示视频
    private PhotoPickConfig.Builder.OnPhotoResultCallback onPhotoResultCallback;

    public PhotoPickBean(){}

    private PhotoPickBean(Parcel in) {
        maxPickSize = in.readInt();
        pickMode = in.readInt();
        spanCount = in.readInt();
        showCamera = in.readByte() != 0;
        clipPhoto = in.readByte() != 0;
        originalPicture = in.readByte() != 0;
        showGif = in.readByte() != 0;
        mediaType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(maxPickSize);
        dest.writeInt(pickMode);
        dest.writeInt(spanCount);
        dest.writeByte((byte) (showCamera ? 1 : 0));
        dest.writeByte((byte) (clipPhoto ? 1 : 0));
        dest.writeByte((byte) (originalPicture ? 1 : 0));
        dest.writeByte((byte) (showGif ? 1 : 0));
        dest.writeInt(mediaType);
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

    @Override
    public int describeContents() {
        return 0;
    }


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

    public boolean isShowGif() {
        return showGif;
    }

    public void setShowGif(boolean showGif) {
        this.showGif = showGif;
    }

    public PhotoPickConfig.Builder.OnPhotoResultCallback getOnPhotoResultCallback() {
        return onPhotoResultCallback;
    }

    public void setOnPhotoResultCallback(PhotoPickConfig.Builder.OnPhotoResultCallback onPhotoResultCallback) {
        this.onPhotoResultCallback = onPhotoResultCallback;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }
}
