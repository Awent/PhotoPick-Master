package com.awen.photo.photopick.util;

import android.graphics.BitmapFactory;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class BitmapUtil {
    /**
     * 检查文件是否损坏
     * Check if the file is corrupted
     *
     * @param filePath
     * @return
     */
    public static boolean checkImgCorrupted(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return (options.mCancel || options.outWidth == -1
                || options.outHeight == -1);
    }
}
