package com.awen.photo.photopick.bean;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public class PhotoPreviewBean implements Parcelable {

    private int position;
    private int maxPickSize;
    private boolean originalPicture;//是否选择的是原图
    private boolean isPreview;//是否是预览

    public PhotoPreviewBean(){}


    private PhotoPreviewBean(Parcel in) {
        position = in.readInt();
        maxPickSize = in.readInt();
        originalPicture = in.readByte() != 0;
        isPreview = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(position);
        dest.writeInt(maxPickSize);
        dest.writeByte((byte) (originalPicture ? 1 : 0));
        dest.writeByte((byte) (isPreview ? 1 : 0));
    }

    public static final Creator<PhotoPreviewBean> CREATOR = new Creator<PhotoPreviewBean>() {
        @Override
        public PhotoPreviewBean createFromParcel(Parcel in) {
            return new PhotoPreviewBean(in);
        }

        @Override
        public PhotoPreviewBean[] newArray(int size) {
            return new PhotoPreviewBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getMaxPickSize() {
        return maxPickSize;
    }

    public void setMaxPickSize(int maxPickSize) {
        this.maxPickSize = maxPickSize;
    }

    public boolean isOriginalPicture() {
        return originalPicture;
    }

    public void setOriginalPicture(boolean originalPicture) {
        this.originalPicture = originalPicture;
    }

    public boolean isPreview() {
        return isPreview;
    }

    public void setPreview(boolean preview) {
        isPreview = preview;
    }
}
