package com.awen.photo;

import android.app.Application;
import android.content.Context;

import com.awen.photo.photopick.util.ImagePipelineConfigFactory;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class App extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //这里用facebook提供的开源图片处理框架,详细用法可参考文档:http://www.fresco-cn.org/
        //或者:http://blog.csdn.net/android_ls/article/details/53137867
        //或者:http://blog.csdn.net/wa991830558/article/details/46005063
        //或者:http://www.lxway.com/4406818246.htm
        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
    }

    public static Context getContext() {
        return context;
    }
}
