package com.simaple;

import android.app.Application;

import com.awen.photo.Awen;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Awen.init(this);
        //下面是配置toolbar颜色和存储图片地址的
//        Awen.init(this,android.R.color.holo_blue_light);
//        Awen.init(this,android.R.color.holo_blue_light,"/storage/xxxx/xxx");
    }
}
