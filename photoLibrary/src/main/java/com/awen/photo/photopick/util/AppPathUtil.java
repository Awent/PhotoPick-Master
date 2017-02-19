package com.awen.photo.photopick.util;

import android.os.Environment;
import android.text.TextUtils;

import com.awen.photo.Awen;
import com.awen.photo.R;

import java.io.File;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class AppPathUtil {

    /**
     * 裁剪头像
     *
     * @return
     */
    public static String getClipPhotoPath() {
        return getPath("clip");
    }

    /**
     * 保存大图到本地的路径地址
     *
     * @return String
     */
    public static String getBigBitmapCachePath() {
        return getPath("Photo");
    }

    private static String getPath(String str) {
        String path = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getPath();
        }
        if (TextUtils.isEmpty(path)) {
            path = Awen.getContext().getCacheDir().getPath();
        }
        //地址如下:path/appname/appname_photo/
        String app_root_name = Awen.getContext().getString(R.string.app_root_name);
        path = path + "/" + app_root_name + "/" + app_root_name + "_" + str + "/";
        exitesFolder(path);
        return path;
    }

    /**
     * 判断文件夹是否存在,不存在则创建
     *
     * @param path
     */
    public static void exitesFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
