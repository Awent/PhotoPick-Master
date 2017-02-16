package com.awen.photo;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.awen.photo.photopick.util.ImagePipelineConfigFactory;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public final class Awen {

    private Awen() {
    }

    @ColorRes
    private static int toolbarBackGround;
    private static Context mContext;

    public static void init(Context context) {
        init(context,android.R.color.holo_red_light);
    }

    public static void init(Context context, @ColorRes int toolbarBackGroundId) {
        //这里用facebook提供的开源图片处理框架,详细用法可参考文档:http://www.fresco-cn.org/
        //或者:http://blog.csdn.net/android_ls/article/details/53137867
        //或者:http://blog.csdn.net/wa991830558/article/details/46005063
        //或者:http://www.lxway.com/4406818246.htm
        Fresco.initialize(context, ImagePipelineConfigFactory.getImagePipelineConfig(context));
        toolbarBackGround = toolbarBackGroundId;
        mContext = context.getApplicationContext();
    }

    @ColorInt
    public static int getToolbarBackGround() {
        return mContext.getResources().getColor(toolbarBackGround);
    }

    public static Context getContext(){
        return mContext;
    }

    public static void checkInit(){
        if(mContext == null){
            throw new NullPointerException("photoLibrary was not initialized,please init in your application");
        }
    }

}
