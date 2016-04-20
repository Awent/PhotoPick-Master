package com.awen.photo.photopick.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.awen.photo.photopick.ui.PhotoPickActivity;

/**
 * 使用方法,hao to use：<br><code>
 * new PickConfig.Builder(this)<br>
 * .pickMode(PickConfig.MODE_MULTIP_PICK)<br>
 * .maxPickSize(30)<br>
 * .spanCount(3)<br>
 * .showCamera(false) //default true<br>
 * .clipPhoto(true)   //default false<br>
 * .build();<br>
 * </code>
 * Created by Awen <Awentljs@gmail.com>
 */
public class PhotoPickConfig {
    public static int DEFAULT_PICKSIZE = 9;//默认可以选择的图片数目

    public static int MODE_SINGLE_PICK = 1;//单选模式

    public static int MODE_MULTIP_PICK = 2;//多选

    public static int DEFAULT_SPANCOUNT = 3;//gridview列数

    public static boolean DEFAULT_SHOW_CAMERA = true;//默认展示拍照那个icon

    public static boolean DEFAULT_START_CLIP = false;//默认不开启图片裁剪

    public final static String EXTRA_STRING_ARRAYLIST = "extra_string_array_list";
    public final static String EXTRA_PICK_BUNDLE = "extra_pick_bundle";
    public final static String EXTRA_SPAN_COUNT = "extra_span_count";
    public final static String EXTRA_PICK_MODE = "extra_pick_mode";
    public final static String EXTRA_MAX_SIZE = "extra_max_size";
    public final static String EXTAR_SHOW_CAMERA = "extra_show_camera";
    public final static String EXTAR_START_CLIP = "extra_start_clip";

    public final static int PICK_REQUEST_CODE = 10507;

    public PhotoPickConfig(Activity context, PhotoPickConfig.Builder builder) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_SPAN_COUNT, builder.spanCount);
        bundle.putInt(EXTRA_PICK_MODE, builder.pickMode);
        bundle.putInt(EXTRA_MAX_SIZE, builder.maxPickSize);
        bundle.putBoolean(EXTAR_SHOW_CAMERA, builder.showCamera);
        bundle.putBoolean(EXTAR_START_CLIP, builder.clipPhoto);
        startPick(context, bundle);
    }

    private void startPick(Activity context, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PICK_BUNDLE, bundle);
        intent.setClass(context, PhotoPickActivity.class);
        context.startActivityForResult(intent, PICK_REQUEST_CODE);
    }

    public static class Builder {
        private Activity context;
        private int spanCount = DEFAULT_SPANCOUNT;
        private int pickMode = MODE_SINGLE_PICK;
        private int maxPickSize = DEFAULT_PICKSIZE;
        private boolean showCamera = DEFAULT_SHOW_CAMERA;
        private boolean clipPhoto = DEFAULT_START_CLIP;

        public Builder(Activity context) {
            if (context == null) {
                throw new NullPointerException("context is null");
            }
            this.context = context;
        }

        /**
         * gridview的列数
         *
         * @param spanCount defult value 3
         * @return
         */
        public PhotoPickConfig.Builder spanCount(int spanCount) {
            this.spanCount = spanCount;
            if (this.spanCount == 0) {
                this.spanCount = DEFAULT_SPANCOUNT;
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
            this.pickMode = pickMode;
            if (this.pickMode == 1) {
                this.maxPickSize = 1;
                this.pickMode = MODE_SINGLE_PICK;
            } else if (this.pickMode == 2) {
                this.clipPhoto = false;
                this.pickMode = MODE_MULTIP_PICK;
            } else {
                throw new IllegalArgumentException("unkonw pickMod : " + this.pickMode);
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
            this.maxPickSize = maxPickSize;
            if (this.maxPickSize == 0) {
                this.maxPickSize = 1;
                this.pickMode = MODE_SINGLE_PICK;
            } else if (this.pickMode == MODE_SINGLE_PICK) {
                this.maxPickSize = 1;
                this.clipPhoto = DEFAULT_START_CLIP;
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
            this.showCamera = showCamera;
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
            this.clipPhoto = clipPhoto;
            return this;
        }

        public PhotoPickConfig build() {
            if (clipPhoto) {
                this.maxPickSize = 1;
                this.pickMode = MODE_SINGLE_PICK;
            }
            return new PhotoPickConfig(context, this);
        }
    }

}
