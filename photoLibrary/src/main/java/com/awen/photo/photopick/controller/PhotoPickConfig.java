package com.awen.photo.photopick.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.awen.photo.Awen;
import com.awen.photo.R;
import com.awen.photo.photopick.bean.PhotoPickBean;
import com.awen.photo.photopick.bean.PhotoResultBean;
import com.awen.photo.photopick.ui.PhotoPickActivity;

/**
 * 使用方法,hao to use：<br><code>
 * new PickConfig.Builder(this)<br>
 * .pickMode(PickConfig.MODE_MULTIP_PICK)<br>
 * .maxPickSize(30)<br>
 * .spanCount(3)<br>
 * .showCamera(false) //default true<br>
 * .clipPhoto(true)   //default false<br>
 * .setOriginalPicture(true)//default false<br>
 * .showGif(true)//default true<br>
 * .build();<br>
 * </code>
 * Created by Awen <Awentljs@gmail.com>
 */
public class PhotoPickConfig {

    public static int MODE_SINGLE_PICK = 1;//单选模式

    public static int MODE_MULTIP_PICK = 2;//多选

    private static int DEFAULT_PICKSIZE = 9;//默认可以选择的图片数目

    private static int DEFAULT_SPANCOUNT = 3;//gridview列数

    private static boolean DEFAULT_SHOW_CAMERA = true;//默认展示拍照那个icon

    private static boolean DEFAULT_START_CLIP = false;//默认不开启图片裁剪

    private static boolean DEFAULT_SHOW_GIF = true;//默认展示gif图片

    public final static String EXTRA_STRING_ARRAYLIST = "extra_string_array_list";
    public final static String EXTRA_PICK_BUNDLE = "extra_pick_bundle";
    public final static String EXTRA_PICK_BEAN = "extra_pick_bean";
    public final static int PICK_REQUEST_CODE = 10507;

    /**
     * 关于数据的回传，新增了{@link Builder#setOnPhotoResultCallback(Builder.OnPhotoResultCallback)}接口,可实现此接口获取回传数据，当然Activity的回传方式也可用，两种方式可同时获取到数据
     * 使用方法,hao to use：<br><code>
     * new PickConfig.Builder(this)<br>
     * .pickMode(PickConfig.MODE_MULTIP_PICK)<br>
     * .maxPickSize(30)<br>
     * .spanCount(3)<br>
     * .showCamera(false) //default true<br>
     * .clipPhoto(true)   //default false<br>
     * .setOriginalPicture(true)//default false<br>
     * .showGif(true)//default true<br>
     * .setOnPhotoResultCallback(OnPhotoResultCallback onPhotoResultCallback)
     * .build();<br>
     * </code>
     * Created by Awen <Awentljs@gmail.com>
     */
    private PhotoPickConfig(Activity context, PhotoPickConfig.Builder builder) {
        if(builder.pickBean == null){
            throw new NullPointerException("builder#pickBean is null");
        }
        PhotoPickActivity.setOnPhotoResultCallback(builder.pickBean.getOnPhotoResultCallback());
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PICK_BEAN,builder.pickBean);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PICK_BUNDLE, bundle);
        intent.setClass(context, PhotoPickActivity.class);
        context.startActivityForResult(intent, PICK_REQUEST_CODE);
        context.overridePendingTransition(R.anim.image_pager_enter_animation, 0);
    }

    public static class Builder {
        private Activity context;
        private PhotoPickBean pickBean;

        public Builder(Activity context) {
            Awen.checkInit();
            if (context == null) {
                throw new NullPointerException("context is null");
            }
            this.context = context;
            pickBean = new PhotoPickBean();
            pickBean.setSpanCount(DEFAULT_SPANCOUNT);//gridview列数
            pickBean.setMaxPickSize(DEFAULT_PICKSIZE);//默认可以选择的图片数目
            pickBean.setPickMode(MODE_SINGLE_PICK);
            pickBean.setShowCamera(DEFAULT_SHOW_CAMERA);//默认展示拍照那个icon
            pickBean.setClipPhoto(DEFAULT_START_CLIP);//默认不开启图片裁剪
            pickBean.setShowGif(DEFAULT_SHOW_GIF);//默认展示gif
        }

        /**
         * gridview的列数
         *
         * @param spanCount defult value 3
         * @return
         */
        public PhotoPickConfig.Builder spanCount(int spanCount) {
            pickBean.setSpanCount(spanCount);
            if (pickBean.getSpanCount() == 0) {
                pickBean.setSpanCount(DEFAULT_SPANCOUNT);
            }
            return this;
        }

        /**
         * 单选还是多选
         *
         * @param pickMode {@link PhotoPickConfig#MODE_SINGLE_PICK} <br> {@link PhotoPickConfig#MODE_MULTIP_PICK}
         * @return
         */
        public PhotoPickConfig.Builder pickMode(int pickMode) {
            pickBean.setPickMode(pickMode);
            if (pickMode == MODE_SINGLE_PICK) {
                pickBean.setMaxPickSize(1);
            } else if (pickMode == MODE_MULTIP_PICK) {
                pickBean.setClipPhoto(false);
            } else {
                throw new IllegalArgumentException("unkonw pickMod : " + pickMode);
            }
            return this;
        }

        /**
         * 最多可以选择的图片数目
         *
         * @param maxPickSize default value is 9
         * @return
         */
        public PhotoPickConfig.Builder maxPickSize(int maxPickSize) {
            pickBean.setMaxPickSize(maxPickSize);
            if (maxPickSize == 0) {
                pickBean.setMaxPickSize(1);
                pickBean.setPickMode(MODE_SINGLE_PICK);
            } else if (pickBean.getPickMode() == MODE_SINGLE_PICK) {
                pickBean.setMaxPickSize(1);
                pickBean.setClipPhoto(DEFAULT_START_CLIP);
            }
            return this;
        }

        /**
         * 是否展示拍照那个icon
         *
         * @param showCamera default true
         * @return
         */
        public PhotoPickConfig.Builder showCamera(boolean showCamera) {
            pickBean.setShowCamera(showCamera);
            return this;
        }

        /**
         * 是否开启选择完图片之后进行图片裁剪<br>
         * 如果传true，pickMode会自动设置为单选
         *
         * @param clipPhoto default false
         * @return
         */
        public PhotoPickConfig.Builder clipPhoto(boolean clipPhoto) {
            pickBean.setClipPhoto(clipPhoto);
            return this;
        }

        /**
         * 是否让用户选择原图
         * @param originalPicture default false
         * @return
         */
        public PhotoPickConfig.Builder setOriginalPicture(boolean originalPicture) {
            //是否设置原图,默认false
            this.pickBean.setOriginalPicture(originalPicture);
            return this;
        }

        /**
         * 是否展示gif图片<br>
         *
         * @param showGif default true
         * @return
         */
        public PhotoPickConfig.Builder showGif(boolean showGif) {
            pickBean.setShowGif(showGif);
            return this;
        }

        /**
         * 如果不想在Activity通过onActivityResult()获取回传的数据，可实现此接口。
         * @param onPhotoResultCallback
         * @return
         */
        public PhotoPickConfig.Builder setOnPhotoResultCallback(OnPhotoResultCallback onPhotoResultCallback){
            pickBean.setOnPhotoResultCallback(onPhotoResultCallback);
            return this;
        }

        public PhotoPickConfig.Builder setPhotoPickBean(PhotoPickBean bean){
            this.pickBean = bean;
            return this;
        }

        public PhotoPickConfig build() {
            if (pickBean.isClipPhoto()) {
                pickBean.setMaxPickSize(1);
                pickBean.setPickMode(MODE_SINGLE_PICK);
            }
            return new PhotoPickConfig(context, this);
        }

        public interface OnPhotoResultCallback{
            void onResult(PhotoResultBean result);
        }
    }

}
