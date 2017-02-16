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
        //下面这个是配置toolbar颜色的
//        Awen.init(this,android.R.color.holo_blue_light);
    }
}
