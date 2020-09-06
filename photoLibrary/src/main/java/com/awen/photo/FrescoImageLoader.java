package com.awen.photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.awen.photo.photopick.ui.VideoPlayActivity;
import com.awen.photo.photopick.util.FileSizeUtil;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.File;
import java.io.IOException;

/**
 * 以下四个方法可能是你想用的
 *
 * @see #setToolbarBackGround(int)
 * @see #setSaveImageLocalPath(String)
 * @see #getSDCacheSizeFormat()
 * @see #clearCaches()
 * Created by Awen <Awentljs@gmail.com>
 */

public final class FrescoImageLoader extends Awen {

    private FrescoImageLoader() {
    }

    public static void evictFromMemoryCache(String url){
        if (url != null && !TextUtils.isEmpty(url)) {
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            Uri uri = Uri.parse(url);
            if (imagePipeline.isInBitmapMemoryCache(uri)) {
                imagePipeline.evictFromMemoryCache(uri);
            }
        }
    }

    /**
     * 图片是否已经存在了
     */
    public static boolean isCached(Context context, Uri uri) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<Boolean> dataSource = imagePipeline.isInDiskCache(uri);
        if (dataSource == null) {
            return false;
        }
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, context);
        BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
        return resource != null && dataSource.getResult() != null && dataSource.getResult();
    }

    /**
     * 本地缓存文件
     */
    public static File getCache(Context context, Uri uri) {
        if (!isCached(context, uri))
            return null;
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, context);
        BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
        return ((FileBinaryResource) resource).getFile();
    }

    public static File getCache(ImageRequest request) {
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(request, null);
        BinaryResource bRes = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
        return ((FileBinaryResource) bRes).getFile();
    }

    public static byte[] getByte(Context context, Uri uri) throws IOException {
        if (!isCached(context, uri))
            return null;
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, null);
        BinaryResource bRes = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
        return bRes.read();
    }

    public static byte[] getByte(ImageRequest request) throws IOException {
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(request, null);
        BinaryResource bRes = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
        return bRes.read();
    }

    /**
     * 清空所有内存缓存
     */
    public static void clearMemoryCaches() {
        Fresco.getImagePipeline().clearMemoryCaches();
    }

    /**
     * 清空所有磁盘缓存
     */
    public static void clearDiskCaches() {
        Fresco.getImagePipeline().clearDiskCaches();
    }

    /**
     * 清除所有缓存（包括内存+磁盘）
     */
    public static void clearCaches() {
        clearMemoryCaches();
        clearDiskCaches();
    }

    /**
     * 获取磁盘上主缓存文件缓存的大小
     */
    public static long getMainFileCache() {
        Fresco.getImagePipelineFactory().getMainFileCache().trimToMinimum();
        return Fresco.getImagePipelineFactory().getMainFileCache().getSize();
    }

    /**
     * 获取磁盘上副缓存（小文件）文件缓存的大小
     */
    public static long getSmallImageFileCache() {
        Fresco.getImagePipelineFactory().getSmallImageFileCache().trimToMinimum();
        return Fresco.getImagePipelineFactory().getSmallImageFileCache().getSize();
    }

    /**
     * 获取磁盘上缓存文件的大小
     */
    public static long getSDCacheSize() {
        return getMainFileCache() + getSmallImageFileCache();
    }

    /**
     * 获取磁盘上缓存文件的大小,带单位，比如：20MB
     */
    public static String getSDCacheSizeFormat() {
        return FileSizeUtil.formatFileSize(getSDCacheSize());
    }

    public static String getFileUrl(String url) {
        return UriUtil.LOCAL_FILE_SCHEME + ":///" + url;
    }

    public static String getResUrl(int resId) {
        return UriUtil.LOCAL_RESOURCE_SCHEME + ":///" + resId;
    }

    public static String getAssetUrl(String assetId) {
        return UriUtil.LOCAL_ASSET_SCHEME + ":///" + assetId;
    }

    /**
     * 启动一个视频播放
     * @param activity
     * @param videoUrl 本地视频地址或网络视频地址
     */
    public static void startVideoActivity(Activity activity, String videoUrl){
        activity.startActivity(new Intent(activity, VideoPlayActivity.class)
                .putExtra("videoUrl", videoUrl));
        activity.overridePendingTransition(R.anim.image_pager_enter_animation, 0);
    }
}
