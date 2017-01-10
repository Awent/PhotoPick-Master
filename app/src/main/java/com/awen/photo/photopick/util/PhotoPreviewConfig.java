package com.awen.photo.photopick.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.awen.photo.photopick.bean.Photo;
import com.awen.photo.photopick.bean.PhotoPreviewBean;
import com.awen.photo.photopick.ui.PhotoPreviewActivity;

import java.util.ArrayList;

/**
 * 仿微信图片预览<br>
 * 如何使用:<br>
 * <code>
 * new PhotoPreviewConfig.Builder((Activity) context)<br>
 * .setPosition(position)<br>
 * .setMaxPickSize(maxPickSize)<br>
 * .setPhotos(photos)<br>
 * .setSelectPhotos(selectPhotos)<br>
 * .build();<br>
 * </code>
 * Created by Awen <Awentljs@gmail.com>
 */

public class PhotoPreviewConfig {
    public static final String EXTRA_BUNDLE = "extra_bundle";
    public static final String EXTRA_BEAN = "extra_bean";
    public final static int REQUEST_CODE = 10504;

    public PhotoPreviewConfig(Activity activity, Builder builder) {
        PhotoPreviewBean photoPreviewBean = builder.bean;
        if (photoPreviewBean == null) {
            throw new NullPointerException("Builder#photoPagerBean is null");
        }
        if (photoPreviewBean.getPhotos() == null || photoPreviewBean.getPhotos().isEmpty()) {
            throw new NullPointerException("bigImageUrls is null or size is 0");
        }
        if (photoPreviewBean.getPosition() > photoPreviewBean.getMaxPickSize()) {
            throw new IndexOutOfBoundsException("show position out maxPickSize size,position = " + photoPreviewBean.getPosition() + ",maxPickSize size = " + photoPreviewBean.getMaxPickSize());
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_BEAN, photoPreviewBean);
        startPreviewActivity(activity, bundle);
    }

    private void startPreviewActivity(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, PhotoPreviewActivity.class);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public static class Builder {
        private Activity context;
        private PhotoPreviewBean bean;

        public Builder(Activity context) {
            if (context == null) {
                throw new NullPointerException("context is null");
            }
            this.context = context;
            bean = new PhotoPreviewBean();
        }

        public Builder setPhotoPreviewBean(PhotoPreviewBean bean) {
            this.bean = bean;
            return this;
        }

        public Builder setPosition(int position) {
            if (position < 0) {
                position = 0;
            }
            bean.setPosition(position);
            return this;
        }

        public Builder setPhotos(ArrayList<Photo> photos) {
            if (photos == null || photos.isEmpty()) {
                throw new NullPointerException("photos is null or size is 0");
            }
            bean.setPhotos(photos);
            return this;
        }

        public Builder setSelectPhotos(ArrayList<String> selectPhotos) {
            if (selectPhotos == null || selectPhotos.isEmpty()) {
                throw new NullPointerException("selectPhotos is null or size is 0");
            }
            bean.setSelectPhotos(selectPhotos);
            return this;
        }

        public Builder setMaxPickSize(int maxPickSize) {
            bean.setMaxPickSize(maxPickSize);
            return this;
        }

        public PhotoPreviewConfig build() {
            return new PhotoPreviewConfig(context, this);
        }

    }
}
