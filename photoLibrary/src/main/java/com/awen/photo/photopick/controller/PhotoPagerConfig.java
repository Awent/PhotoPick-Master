package com.awen.photo.photopick.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.awen.photo.Awen;
import com.awen.photo.R;
import com.awen.photo.photopick.bean.PhotoPagerBean;
import com.awen.photo.photopick.ui.PhotoPagerActivity;
import com.awen.photo.photopick.util.AppPathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认不开启保存图片功能，但是会有默认的保存图片地址,默认展示第一张图片<br>
 * 注意查看大图传本地图片路径的时候要加上scheme，既是加上这个："file://"，这样显示的效果更佳<br>
 * 可以这样使用,how to use：<br>
 * <code>new PhotoPagerConfig<br>
 * .Builder(this)<br>
 * .setBigImageUrls(list)<br>
 * .setLowImageUrls(list)<br>
 * .setPosition(4)<br>
 * .setSavaImage(true)<br>
 * .setSaveImageLocalPath("/storage/xxxx/xxx")<br>
 * .build();<br></code>
 * Created by Awen <Awentljs@gmail.com>
 */
public class PhotoPagerConfig {
    public static final String EXTRA_PAGER_BUNDLE = "extra_pager_bundle";
    public static final String EXTRA_USER_BUNDLE = "extra_user_bundle";
    public static final String EXTRA_PAGER_BEAN = "extra_pager_bean";

    private PhotoPagerConfig(Activity activity, Builder builder) {
        PhotoPagerBean photoPagerBean = builder.photoPagerBean;
        if (photoPagerBean == null) {
            throw new NullPointerException("Builder#photoPagerBean is null");
        }
        if (photoPagerBean.getBigImgUrls() == null || photoPagerBean.getBigImgUrls().isEmpty()) {
            throw new NullPointerException("bigImageUrls is null or size is 0");
        }
        if (photoPagerBean.getPagePosition() > photoPagerBean.getBigImgUrls().size()) {
            throw new IndexOutOfBoundsException("show position out bigImageUrls size,position = " + photoPagerBean.getPagePosition() + ",bigImageUrls size = " + photoPagerBean.getBigImgUrls().size());
        }
        PhotoPagerActivity.setOnPhotoSaveCallback(builder.photoPagerBean.getOnPhotoSaveCallback());
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PAGER_BEAN, photoPagerBean);
        Intent intent = new Intent(activity, builder.clazz);
        intent.putExtra(EXTRA_PAGER_BUNDLE, bundle);
        if (builder.bundle != null) {
            intent.putExtra(EXTRA_USER_BUNDLE, builder.bundle);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.image_pager_enter_animation, 0);
    }

    public static class Builder {
        private Activity context;
        private PhotoPagerBean photoPagerBean;
        private Class<?> clazz;
        private Bundle bundle;

        public Builder(Activity context) {
            this(context, PhotoPagerActivity.class);
        }

        public Builder(Activity context, Class<?> clazz) {
            Awen.checkInit();
            if (context == null) {
                throw new NullPointerException("activity is null");
            }
            this.clazz = clazz;
            this.context = context;
            photoPagerBean = new PhotoPagerBean();
            photoPagerBean.setPagePosition(0);//默认展示第1张图片
            photoPagerBean.setSaveImage(false);//默认不开启保存图片到本地
            photoPagerBean.setSaveImageLocalPath(AppPathUtil.getBigBitmapCachePath());//默认保存到本地的图片地址
            photoPagerBean.setOpenDownAnimate(true);
        }

        /**
         * 默认展示第一张图片
         *
         * @param position 默认展示第一张图片
         * @return Builder
         */
        public Builder setPosition(int position) {
            if (position < 0) {
                position = 0;
            }
            photoPagerBean.setPagePosition(position);
            return this;
        }

        /**
         * 大图图片的url,后续可扩展更多
         *
         * @param bigImgUrls 图片的url
         * @return Builder
         */
        public Builder setBigImageUrls(List<String> bigImgUrls) {
            if (bigImgUrls == null || bigImgUrls.isEmpty()) {
                throw new NullPointerException("imageUrls is null or size is 0");
            }
            photoPagerBean.setBigImgUrls((ArrayList<String>) bigImgUrls);
            return this;
        }

        /**
         * 默认不开启图片保存到本地功能
         *
         * @param savaImage true is open,false is close, default false;
         * @return Builder
         */
        public Builder setSavaImage(boolean savaImage) {
            photoPagerBean.setSaveImage(savaImage);
            return this;
        }

        /**
         * 会有个默认的地址，不传也可以
         *
         * @param saveImageLocalPath 保存图片到本地的地址
         * @return Builder
         */
        public Builder setSaveImageLocalPath(String saveImageLocalPath) {
            photoPagerBean.setSaveImageLocalPath(saveImageLocalPath);
            return this;
        }

        /**
         * p:更名为small,不使用low了，感觉很不好
         *
         * @param lowImgUrls 小图图片的url
         * @return Builder
         * @deprecated use {@link #setSmallImageUrls(List)}
         */
        public Builder setLowImageUrls(List<String> lowImgUrls) {
            setSmallImageUrls(lowImgUrls);
            return this;
        }

        /**
         * 小图图片的url,后续可扩展更多
         *
         * @param smallImgUrls 小图图片的url
         * @return Builder
         */
        public Builder setSmallImageUrls(List<String> smallImgUrls) {
            if (smallImgUrls == null || smallImgUrls.isEmpty()) {
                throw new NullPointerException("smallImgUrls is null or size is 0");
            }
            photoPagerBean.setSmallImgUrls((ArrayList<String>) smallImgUrls);
            return this;
        }

        /**
         * 一张一张大图add进ArrayList
         *
         * @param bigImageUrl 大图url
         * @return Builder
         */
        public Builder addSingleBigImageUrl(String bigImageUrl) {
            photoPagerBean.addSingleBigImageUrl(bigImageUrl);
            return this;
        }

        /**
         * 一张一张小图add进ArrayList
         *
         * @param smallImageUrl 小图url
         * @return Builder
         */
        public Builder addSingleSmallImageUrl(String smallImageUrl) {
            photoPagerBean.addSingleSmallImageUrl(smallImageUrl);
            return this;
        }

        /**
         * @deprecated use {@link #addSingleSmallImageUrl(String)}
         */
        public Builder addSingleLowImageUrl(String lowImageUrl) {
            photoPagerBean.addSingleLowImageUrl(lowImageUrl);
            return this;
        }


        /**
         * 设置用户想传递的bundle
         *
         * @param bundle
         * @return
         */
        public Builder setBundle(Bundle bundle) {
            this.bundle = bundle;
            return this;
        }

        /**
         * 是否开启下滑关闭activity，默认开启。类似微信的图片浏览，可下滑关闭一样，但是没有图片归位效果
         *
         * @param isOpenDownAnimate default true
         * @return
         */
        public Builder setOpenDownAnimate(boolean isOpenDownAnimate) {
            photoPagerBean.setOpenDownAnimate(isOpenDownAnimate);
            return this;
        }

        /**
         * 你也可以不设置任何参数，除了此参数以为，{@link PhotoPagerBean}是参数配置的一个实体，你可以直接传个实体进来也可以
         *
         * @param photoPagerBean PhotoPagerBean
         * @return Builder
         */
        public Builder setPhotoPagerBean(PhotoPagerBean photoPagerBean) {
            if (photoPagerBean == null) {
                throw new NullPointerException("photoPagerBean is null");
            }
            this.photoPagerBean = photoPagerBean;
            return this;
        }

        /**
         * 保存图片到图库的回调
         * @param onPhotoSaveCallback
         * @return
         */
        public Builder setOnPhotoSaveCallback(OnPhotoSaveCallback onPhotoSaveCallback){
            photoPagerBean.setOnPhotoSaveCallback(onPhotoSaveCallback);
            return this;
        }

        public PhotoPagerConfig build() {
            return new PhotoPagerConfig(context, this);
        }

        /**
         * 保存图片到本地图库的回调
         */
        public interface OnPhotoSaveCallback {
            /**
             * 成功则返回路径，失败返回null
             * @param localFilePath
             */
            void onSaveImageResult(String localFilePath);
        }
    }

}
