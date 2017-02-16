package com.awen.photo.photopick.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.awen.photo.Awen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class ImageUtils {

    /**
     * 按比例压缩图片
     *
     * @param path
     * @param w
     * @param h
     * @return
     */
    public static Bitmap getBitmap(String path, int w, int h) {
        Bitmap bit = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // opts.inJustDecodeBounds = true
        // ,设置该属性为true，不会真的返回一个Bitmap给你，它仅仅会把它的宽，高取回来给你，
        // 这样就不会占用太多的内存，也就不会那么频繁的发生OOM了
        opts.inJustDecodeBounds = true;
        bit = BitmapFactory.decodeFile(path, opts);
        int test = 1;
        if (opts.outWidth > opts.outHeight) {
            if (opts.outWidth >= w)
                test = opts.outWidth / w;
            opts.inSampleSize = test; // 限制处理后的图片最大宽为w*2
        } else {
            if (opts.outHeight >= h)
                test = opts.outHeight / h;
            opts.inSampleSize = test; // 限制处理后的图片最大高为h*2
        }
        opts.inJustDecodeBounds = false;
        bit = BitmapFactory.decodeFile(path, opts);
        return bit;
    }

    /**
     * 保存图片
     *
     * @param bmp
     * @return
     */
    public static boolean saveImage2(String filePath, Bitmap bmp) {
        if (bmp == null)
            return false;
        File f = new File(filePath);
        if (f.exists()) {
            f.delete(); // 删除原图片
        }
        if (!f.isFile()) {
            String dir = filePath.substring(0, filePath.lastIndexOf("/"));
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    return false;
                }
            }
            FileOutputStream fOut = null;
            boolean isSuccesse = false;
            try {
                f.createNewFile();
                fOut = new FileOutputStream(f);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                isSuccesse = true;
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                if (fOut != null) {
                    try {
                        fOut.flush();
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return isSuccesse;
        }
        return false;
    }

    /**
     * 保存图片，并且可以再手机的图库中看到
     *
     * @param filePath 图片的本地保存路径，包括图片名
     * @param bmp      Bitmap
     * @return true:保存成功，false:保存失败
     */
    public static boolean saveImageToGallery(String filePath, Bitmap bmp) {
        // 首先保存图片
        if (bmp == null)
            return false;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete(); // 删除原图片
        }
        String dir;
        if (!file.isFile()) {
            dir = filePath.substring(0, filePath.lastIndexOf("/"));
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    return false;
                }
            }
            FileOutputStream fOut = null;
            boolean isSuccesse = false;
            try {
                file.createNewFile();
                fOut = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                // 最后通知图库更新
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                Awen.getContext().sendBroadcast(intent);
                isSuccesse = true;
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                if (fOut != null) {
                    try {
                        fOut.flush();
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return isSuccesse;
        }
        return false;
    }

    /**
     * 保存图片，并且可以再手机的图库中看到
     *
     * @param filePath 图片的本地保存路径，包括图片名
     * @return true:保存成功，false:保存失败
     */
    public static boolean saveImageToGallery(String filePath, byte[] b) {
        // 首先保存图片
        if (b == null)
            return false;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete(); // 删除原图片
        }
        String dir;
        if (!file.isFile()) {
            dir = filePath.substring(0, filePath.lastIndexOf("/"));
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    return false;
                }
            }
            FileOutputStream fOut = null;
            boolean isSuccesse = false;
            try {
                file.createNewFile();
                fOut = new FileOutputStream(file);
                fOut.write(b);
                // 最后通知图库更新
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                Awen.getContext().sendBroadcast(intent);
                isSuccesse = true;
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                if (fOut != null) {
                    try {
                        fOut.flush();
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return isSuccesse;
        }
        return false;
    }

}
