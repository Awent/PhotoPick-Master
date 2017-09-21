package com.awen.photo;

import android.net.Uri;

import com.awen.photo.photopick.util.FileSizeUtil;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

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
}
