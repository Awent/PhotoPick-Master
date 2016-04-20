package com.awen.photo.photopick.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.awen.photo.R;
import com.awen.photo.photopick.ui.PhotoPagerActivity;

import java.util.ArrayList;

/**
 * 默认不开启保存图片功能，但是会有默认的保存图片地址,默认展示第一张图片<br>
 * 注意查看大图传本地图片路径的时候要加上scheme，既是加上这个："file://"，这样显示的效果更佳<br>
 * 可以这样使用,how to use：<br>
 * new PhotoPagerConfig<br>
 * .Builder(this)<br>
 * .setImageUrls(list)<br>
 * .setPosition(4)<br>
 * .setSavaImage(true)<br>
 * .setSaveImageLocalPath("/storage/xxxx/xxx")<br>
 * .build();<br>
 * Created by Awen <Awentljs@gmail.com>
 */
public class PhotoPagerConfig {
    public static final int DEFAULT_POSITION = 0; //默认展示第1张图片

    public static final boolean SAVA_IMAGE = false;//默认不开启保存图片到本地

    /**默认保存到本地的图片地址*/
    public static final String SAVE_IMAGE_LOCAL_PATH = AppPathUtil.getBigBitmapCachePath();

    public static final String EXTRA_PAGER_BUNDLE = "extra_pager_bundle";
    public static final String EXTRA_DEFAULT_POSITION = "extra_default_position";
    public static final String EXTRA_SAVA_IMAGE = "extra_save_image";
    public static final String EXTRA_SAVE_IMAGE_LOCAL_PATH = "extra_save_image_local_path";
    public static final String EXTRA_IMAGE_URLS = "extra_image_urls";

    public PhotoPagerConfig(Activity activity,Builder builder){
        if(builder.imageUrls == null || builder.imageUrls.isEmpty()){
            throw new NullPointerException("imageUrls is null or size is 0");
        }
        if(builder.position > builder.imageUrls.size()){
            throw new IndexOutOfBoundsException("show position out imageUrls size,position = " + builder.position + ",imageUrls size = " + builder.imageUrls.size());
        }
        splitImagePath(builder.imageUrls);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_DEFAULT_POSITION,builder.position);
        bundle.putBoolean(EXTRA_SAVA_IMAGE,builder.saveImage);
        bundle.putString(EXTRA_SAVE_IMAGE_LOCAL_PATH,builder.saveImageLocalPath);
        bundle.putStringArrayList(EXTRA_IMAGE_URLS,builder.imageUrls);
        openImageBrower(activity,bundle);
    }

    private void openImageBrower(Activity activity,Bundle bundle) {
        Intent intent = new Intent(activity, PhotoPagerActivity.class);
        intent.putExtra(EXTRA_PAGER_BUNDLE,bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.image_pager_enter_animation, 0);
    }

    public static class Builder{
        private Activity context;
        private int position = DEFAULT_POSITION;
        private ArrayList<String> imageUrls;
        private boolean saveImage = SAVA_IMAGE;
        private String saveImageLocalPath = SAVE_IMAGE_LOCAL_PATH;
        public Builder(Activity context){
            if(context == null){
                throw new NullPointerException("activity is null");
            }
            this.context = context;
        }

        /**
         * 默认展示第一张图片
         * @param position 默认展示第一张图片
         * @return Builder
         */
        public Builder setPosition(int position){
            if(position < 0){
                position = 0;
            }
            this.position = position;
            return this;
        }

        /**
         * 图片的url,后续可扩展更多
         * @param imageUrls 图片的url
         * @return Builder
         */
        public Builder setImageUrls(ArrayList<String> imageUrls){
            if(imageUrls == null || imageUrls.isEmpty()){
                throw new NullPointerException("imageUrls is null or size is 0");
            }
            this.imageUrls = imageUrls;
            return this;
        }

        /**
         * 默认不开启图片保存到本地功能
         * @param savaImage true is open,false is close, default false;
         * @return Builder
         */
        public Builder setSavaImage(boolean savaImage){
            this.saveImage = savaImage;
            return this;
        }

        /**
         * 会有个默认的地址，不传也可以
         * @param saveImageLocalPath 保存图片到本地的地址
         * @return Builder
         */
        public Builder setSaveImageLocalPath(String saveImageLocalPath){
            this.saveImageLocalPath = saveImageLocalPath;
            return this;
        }

        public PhotoPagerConfig build(){
            return new PhotoPagerConfig(context,this);
        }
    }

    /**
     * 这个是对后台返回的特定图片地址进行裁剪,如果不需要完全可以去掉
     * @param urls ArrayList
     */
    private void splitImagePath(ArrayList<String> urls) {
        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);
            if (url != null) {
                int end = url.lastIndexOf("&");
                if (end != -1) {
                    urls.set(i, url.substring(0, end));
                }
            }
        }
    }
}
