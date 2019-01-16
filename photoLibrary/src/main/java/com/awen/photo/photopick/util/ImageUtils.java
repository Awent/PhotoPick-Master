package com.awen.photo.photopick.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 读取照片旋转角度
     *
     * @param angle  被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    /**
     * 按比例压缩图片
     *
     * @param path
     * @param w
     * @param h
     * @return
     */
    public static Bitmap getBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        int angel = readPictureDegree(path);
        // opts.inJustDecodeBounds = true
        // ,设置该属性为true，不会真的返回一个Bitmap给你，它仅仅会把它的宽，高取回来给你，
        // 这样就不会占用太多的内存，也就不会那么频繁的发生OOM了
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
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
        return rotaingImageView(angel, BitmapFactory.decodeFile(path, opts));
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
        Log.e("filePath", "filePath = " + filePath);
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
                fOut.flush();
                // 最后通知图库更新
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                Awen.getContext().sendBroadcast(intent);
                isSuccesse = true;
            } catch (Exception e1) {
                if (file.exists()) {
                    file.delete(); // 删除原图片
                }
                e1.printStackTrace();
            } finally {
                if (fOut != null) {
                    try {
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
