package com.awen.photo.photopick.util;

import android.graphics.BitmapFactory;

import com.awen.photo.photopick.bean.Photo;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class BitmapUtil {
    /**
     * 检查文件是否损坏
     * Check if the file is corrupted
     *
     * @return false为不损坏，true为损坏
     */
    public static boolean checkImgCorrupted(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return (options.mCancel || options.outWidth == -1 || options.outHeight == -1);
    }

    /**
     * 检查文件是否损坏,并且给photo添加上长宽
     * Check if the file is corrupted
     *
     * @return false为不损坏，true为损坏
     */
    public static boolean checkImgCorrupted(Photo photo, String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        if(options.mCancel || options.outWidth == -1 || options.outHeight == -1){
            return true;
        }else {
            photo.setWidth(options.outWidth);
            photo.setHeight(options.outHeight);
            return false;
        }
    }

    public static int[] getImageSize(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return new int[]{options.outWidth,options.outHeight};
    }
}
