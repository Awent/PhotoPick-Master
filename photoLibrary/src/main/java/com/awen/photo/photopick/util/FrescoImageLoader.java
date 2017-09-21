package com.awen.photo.photopick.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.animated.base.AnimatedImageResult;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableAnimatedImage;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.memory.PooledByteBufferInputStream;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public class FrescoImageLoader {

    //加载网络图片相关的方法-start
    public static void loadImage(SimpleDraweeView simpleDraweeView, String url) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, 0, 0, null, null, false);
    }

    public static void loadImage(SimpleDraweeView simpleDraweeView, String url, final int reqWidth, final int reqHeight) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, null, null, false);
    }

    public static void loadImage(SimpleDraweeView simpleDraweeView, String url, BasePostprocessor processor) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, 0, 0, processor, null, false);
    }

    public static void loadImage(SimpleDraweeView simpleDraweeView, String url,
                                 final int reqWidth, final int reqHeight, BasePostprocessor processor) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, processor, null, false);
    }

    public static void loadImage(SimpleDraweeView simpleDraweeView, String url, ControllerListener<ImageInfo> controllerListener) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, 0, 0, null, controllerListener, false);
    }

    public static void loadImageSmall(SimpleDraweeView simpleDraweeView, String url) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, 0, 0, null, null, true);
    }

    public static void loadImageSmall(SimpleDraweeView simpleDraweeView, String url, final int reqWidth, final int reqHeight) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, null, null, true);
    }

    public static void loadImageSmall(SimpleDraweeView simpleDraweeView, String url, BasePostprocessor processor) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, 0, 0, processor, null, true);
    }

    public static void loadImageSmall(SimpleDraweeView simpleDraweeView, String url,
                                      final int reqWidth, final int reqHeight, BasePostprocessor processor) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, processor, null, true);
    }
    //加载网络图片相关的方法-end

    //加载本地文件相关的方法-start
    public static void loadFile(final SimpleDraweeView simpleDraweeView, String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(filePath)
                .build();
        loadImage(simpleDraweeView, uri, 0, 0, null, null, false);
    }

    public static void loadFile(final SimpleDraweeView simpleDraweeView, String filePath, final int reqWidth, final int reqHeight) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(filePath)
                .build();

        BaseControllerListener<ImageInfo> controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }

                ViewGroup.LayoutParams vp = simpleDraweeView.getLayoutParams();
                vp.width = reqWidth;
                vp.height = reqHeight;
                simpleDraweeView.requestLayout();
            }
        };
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, null, controllerListener, false);
    }

    public static void loadFile(SimpleDraweeView simpleDraweeView, String filePath, BasePostprocessor processor) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(filePath)
                .build();
        loadImage(simpleDraweeView, uri, 0, 0, processor, null, false);
    }

    public static void loadFile(SimpleDraweeView simpleDraweeView, String filePath,
                                final int reqWidth, final int reqHeight, BasePostprocessor processor) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(filePath)
                .build();

        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, processor, null, false);
    }
    //加载本地文件相关的方法-end

    //加载本地res下面资源相关的方法-start
    public static void loadDrawable(SimpleDraweeView simpleDraweeView, int resId) {
        if (resId == 0 || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build();

        loadImage(simpleDraweeView, uri, 0, 0, null, null, false);
    }

    public static void loadDrawable(SimpleDraweeView simpleDraweeView, int resId, final int reqWidth, final int reqHeight) {
        if (resId == 0 || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build();
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, null, null, false);
    }

    public static void loadDrawable(SimpleDraweeView simpleDraweeView, int resId, BasePostprocessor processor) {
        if (resId == 0 || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build();
        loadImage(simpleDraweeView, uri, 0, 0, processor, null, false);
    }

    public static void loadDrawable(SimpleDraweeView simpleDraweeView, int resId,
                                    final int reqWidth, final int reqHeight, BasePostprocessor processor) {
        if (resId == 0 || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build();
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, processor, null, false);
    }
    //加载本地res下面资源相关的方法-end

    //加载本地asset下面资源相关的方法-start
    public static void loadAssetDrawable(SimpleDraweeView simpleDraweeView, String filename) {
        if (filename == null || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_ASSET_SCHEME)
                .path(filename)
                .build();
        loadImage(simpleDraweeView, uri, 0, 0, null, null, false);
    }

    public static void loadAssetDrawable(SimpleDraweeView simpleDraweeView, String filename, final int reqWidth, final int reqHeight) {
        if (filename == null || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_ASSET_SCHEME)
                .path(filename)
                .build();
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, null, null, false);
    }

    public static void loadAssetDrawable(SimpleDraweeView simpleDraweeView, String filename, BasePostprocessor processor) {
        if (filename == null || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_ASSET_SCHEME)
                .path(filename)
                .build();
        loadImage(simpleDraweeView, uri, 0, 0, processor, null, false);
    }

    public static void loadAssetDrawable(SimpleDraweeView simpleDraweeView, String filename,
                                         final int reqWidth, final int reqHeight, BasePostprocessor processor) {
        if (filename == null || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_ASSET_SCHEME)
                .path(filename)
                .build();
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, processor, null, false);
    }
    //加载本地asset下面资源相关的方法-end

    public static void loadImage(SimpleDraweeView simpleDraweeView,
                                 Uri uri,
                                 final int reqWidth,
                                 final int reqHeight,
                                 BasePostprocessor postprocessor,
                                 ControllerListener<ImageInfo> controllerListener,
                                 boolean isSmall) {

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri);
        imageRequestBuilder.setRotationOptions(RotationOptions.autoRotate());

        // 不支持图片渐进式加载，理由：https://github.com/facebook/fresco/issues/1204
        imageRequestBuilder.setProgressiveRenderingEnabled(false);

        if (isSmall) {
            imageRequestBuilder.setCacheChoice(ImageRequest.CacheChoice.SMALL);
        }

        if (reqWidth > 0 && reqHeight > 0) {
            imageRequestBuilder.setResizeOptions(new ResizeOptions(reqWidth, reqHeight));
        }

        if (UriUtil.isLocalFileUri(uri)) {
            imageRequestBuilder.setLocalThumbnailPreviewsEnabled(true);
        }

        if (postprocessor != null) {
            imageRequestBuilder.setPostprocessor(postprocessor);
        }

        ImageRequest imageRequest = imageRequestBuilder.build();

        PipelineDraweeControllerBuilder draweeControllerBuilder = Fresco.newDraweeControllerBuilder();
        draweeControllerBuilder.setOldController(simpleDraweeView.getController());
        draweeControllerBuilder.setImageRequest(imageRequest);

        if (controllerListener != null) {
            draweeControllerBuilder.setControllerListener(controllerListener);
        }

        draweeControllerBuilder.setTapToRetryEnabled(true); // 开启重试功能
        draweeControllerBuilder.setAutoPlayAnimations(true); // 自动播放gif动画
        DraweeController draweeController = draweeControllerBuilder.build();
        simpleDraweeView.setController(draweeController);
    }

}
