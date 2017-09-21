package com.awen.photo.photopick.util;

import java.text.DecimalFormat;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public class FileSizeUtil {

    private static DecimalFormat df = new DecimalFormat("#.00");
    /**
     * 转换文件大小
     */
    public static String formatFileSize(long fileS) {
        if (fileS == 0) {
            return "0B";
        }
        String fileSizeString;
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}
