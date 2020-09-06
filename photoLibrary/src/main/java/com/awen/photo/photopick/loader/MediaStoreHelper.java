package com.awen.photo.photopick.loader;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.awen.photo.photopick.bean.PhotoPickBean;
import com.awen.photo.photopick.data.Data;
import com.awen.photo.photopick.bean.PhotoDirectory;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class MediaStoreHelper {

    /**
     * 第一种方式
     *
     * @param context        Activity
     * @param resultCallback PhotosResultCallback
     */
    public static void getPhotoDirs(final Activity context, final PhotoPickBean config, final PhotosResultCallback resultCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver contentResolver = context.getContentResolver();
                int mediaType = config.getMediaType();
                PhotoDirectory videoDir = null;
                List<PhotoDirectory> directories = null;
                if (mediaType != MediaType.ONLY_IMAGE) {//不是只显示图片，要查询视频
                    //视频
                    VideoCursorLoader vLoader = new VideoCursorLoader();
                    Cursor vCursor = contentResolver.query(vLoader.getUri(),
                            vLoader.getProjection(),
                            vLoader.getSelection(),
                            vLoader.getSelectionArgs(),
                            vLoader.getSortOrder());
                    if (vCursor != null) {
                        videoDir = Data.getDataFromVideoCursor(context, vCursor);
                        vCursor.close();
                    }
                }

                if (mediaType != MediaType.ONLY_VIDEO) {//不是只显示视频，要查询图片
                    //图片
                    PhotoCursorLoader loader = new PhotoCursorLoader();
                    loader.setShowGif(config.isShowGif());
                    Cursor cursor = contentResolver.query(loader.getUri(),
                            loader.getProjection(),
                            loader.getSelection(),
                            loader.getSelectionArgs(),
                            loader.getSortOrder());
                    if (cursor != null) {
                        directories = Data.getDataFromCursor(context, cursor);
                        cursor.close();
                    }
                }

                if (directories == null) {
                    directories = new ArrayList<>();
                }
                if (videoDir != null && videoDir.getPhotos().size() > 0) {
                    if (directories.size() > 0) {
                        directories.add(1, videoDir);
                        Data.mergePhotoAndVideo(context,directories.get(0), videoDir);
                    } else {
                        directories.add(videoDir);
                    }
                }

                if (resultCallback != null) {
                    resultCallback.onResultCallback(directories);
                }
            }
        }).start();
    }

    /**
     * 第二种方式
     *
     * @param activity       AppCompatActivity
     * @param args           Bundle
     * @param resultCallback PhotosResultCallback
     */
    public static void getPhotoDirs(final AppCompatActivity activity, final Bundle args, final PhotosResultCallback resultCallback) {
        activity.getSupportLoaderManager()
                .initLoader(0, args, new PhotoDirLoaderCallbacks(activity, resultCallback));

    }

    static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private Context context;
        private PhotosResultCallback resultCallback;

        public PhotoDirLoaderCallbacks(Context context, PhotosResultCallback resultCallback) {
            this.context = context;
            this.resultCallback = resultCallback;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(context);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data == null) return;

            List<PhotoDirectory> directories = Data.getDataFromCursor(context, data);
            data.close();

            if (resultCallback != null) {
                resultCallback.onResultCallback(directories);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }


    public interface PhotosResultCallback {
        void onResultCallback(List<PhotoDirectory> directories);
    }

}
