package com.simaple;

import android.app.Application;

import com.awen.photo.FrescoImageLoader;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FrescoImageLoader.init(this);
        //下面是配置toolbar颜色和存储图片地址的
//        FrescoImageLoader.init(this,android.R.color.holo_blue_light);
//        FrescoImageLoader.init(this,android.R.color.holo_blue_light,"/storage/xxxx/xxx");
    }
}
