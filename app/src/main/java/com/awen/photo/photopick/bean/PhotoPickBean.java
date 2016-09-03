package com.awen.photo.photopick.bean;

import java.io.Serializable;

/**
 * Created by Awen <Awentljs@gmail.com>.
 */
public class PhotoPickBean implements Serializable {

    private int maxPickSize;    //最多可以选择多少张图片
    private int pickMode;       //单选还是多选
    private int spanCount;      //recyclerview有多少列
    private boolean showCamera;//是否展示拍照icon
    private boolean clipPhoto; //是否启动裁剪图片

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
}
