package com.awen.photo.photopick.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public class ViewUtil {

    public static boolean isNavigationBarShow(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y!=size.y;
        }else {
            boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return menu || back;
        }
    }

    public static int getNavigationBarHeight(Activity activity) {
        if (!isNavigationBarShow(activity)){
            return 0;
        }
        return activity.getResources().getDimensionPixelSize(activity.getResources().getIdentifier("navigation_bar_height","dimen", "android"));
    }


    public static int getSceenHeight(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight()+getNavigationBarHeight(activity);
    }

    public static int getDisplayHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int[] getDisplayWidthAndHeight(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return new int[]{displayMetrics.widthPixels,displayMetrics.heightPixels};
    }

}
