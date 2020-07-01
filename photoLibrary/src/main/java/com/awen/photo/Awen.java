package com.awen.photo;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import com.awen.photo.photopick.util.FileSizeUtil;
import com.awen.photo.photopick.util.ImagePipelineConfigFactory;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public class Awen {

    protected Awen() {
    }

    @ColorRes
    private static int toolbarBackGround;
    private static Context mContext;
    /**
     * 保存图片到本地的地址
     */
    private static String path;

    public static void init(Context context) {
        init(context, android.R.color.holo_red_light);
    }

    public static void init(Context context, @ColorRes int toolbarBackGroundId) {
        init(context, toolbarBackGroundId, null);
    }

    /**
     * @param context
     * @param toolbarBackGroundId
     * @param saveImageLocalPath  保存图片的路径地址，可以不设置
     */
    public static void init(Context context, @ColorRes int toolbarBackGroundId, String saveImageLocalPath) {
        init(context, toolbarBackGroundId, saveImageLocalPath, ImagePipelineConfigFactory.getImagePipelineConfig(context));
    }

    /**
     * @param context
     * @param toolbarBackGroundId
     * @param saveImageLocalPath  保存图片的路径地址，可以不设置
     * @param imagePipelineConfig Fresco初始化配置
     */
    public static void init(Context context, @ColorRes int toolbarBackGroundId, String saveImageLocalPath, ImagePipelineConfig imagePipelineConfig) {
        if (mContext != null) {//说明已经初始化过了,不用重复初始化
            return;
        }
        //这里用facebook提供的开源图片处理框架,详细用法可参考文档:http://www.fresco-cn.org/
        //或者:http://blog.csdn.net/android_ls/article/details/53137867
        //或者:http://blog.csdn.net/wa991830558/article/details/46005063
        //或者:http://www.lxway.com/4406818246.htm
        if (!Fresco.hasBeenInitialized()) {
            Fresco.initialize(context, imagePipelineConfig);
        }
        toolbarBackGround = toolbarBackGroundId;
        mContext = context.getApplicationContext();
        path = saveImageLocalPath;
    }

    public static void checkInit() {
        if (mContext == null) {
            throw new NullPointerException("photoLibrary was not initialized,please init in your Application");
        }
    }

    public static void destroy() {
        mContext = null;
        path = null;
    }

    @ColorInt
    public static int getToolbarBackGround() {
        return mContext.getResources().getColor(toolbarBackGround);
    }

    public static Context getContext() {
        return mContext;
    }

    public static String getSaveImageLocalPath() {
        return path;
    }

    /**
     * 设置toolbar颜色
     *
     * @param toolbarBackGroundId resId
     */
    public static void setToolbarBackGround(@ColorRes int toolbarBackGroundId) {
        toolbarBackGround = toolbarBackGroundId;
    }

    /**
     * 查看网络大图时，你想把图片保存的地址,保存的图片是可以在手机图库可以看到的
     *
     * @param saveImageLocalPath 大图存储的地址
     */
    public static void setSaveImageLocalPath(String saveImageLocalPath) {
        path = saveImageLocalPath;
    }

}
