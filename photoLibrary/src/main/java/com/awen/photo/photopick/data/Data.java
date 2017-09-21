package com.awen.photo.photopick.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.awen.photo.R;
import com.awen.photo.photopick.bean.Photo;
import com.awen.photo.photopick.bean.PhotoDirectory;
import com.awen.photo.photopick.util.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.SIZE;
import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class Data {
    public final static int INDEX_ALL_PHOTOS = 0;

    public static List<PhotoDirectory> getDataFromCursor(Context context, Cursor data) {
        List<PhotoDirectory> directories = new ArrayList<>();
        PhotoDirectory photoDirectoryAll = new PhotoDirectory();
        photoDirectoryAll.setName(context.getString(R.string.all_photo));
        photoDirectoryAll.setId("ALL");

        while (data.moveToNext()) {
            int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
            String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
            String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
            String path = data.getString(data.getColumnIndexOrThrow(DATA));
            long size = data.getLong(data.getColumnIndexOrThrow(SIZE));
            String mimeType = data.getString(data.getColumnIndexOrThrow(MIME_TYPE));
            Photo photo = new Photo();
            if(BitmapUtil.checkImgCorrupted(photo,path)){
                continue;
            }
            photo.setId(imageId);
            photo.setPath(path);
            photo.setSize(size);
            photo.setMimeType(mimeType);
            PhotoDirectory photoDirectory = new PhotoDirectory();
            photoDirectory.setId(bucketId);
            photoDirectory.setName(name);
            if (!directories.contains(photoDirectory)) {
                photoDirectory.setCoverPath(path);
                photoDirectory.addPhoto(photo);
                photoDirectory.setDateAdded(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
                directories.add(photoDirectory);
            } else {
                directories.get(directories.indexOf(photoDirectory)).addPhoto(photo);
            }
            photoDirectoryAll.addPhoto(photo);
        }
        if (photoDirectoryAll.getPhotoPaths().size() > 0) {
            photoDirectoryAll.setCoverPath(photoDirectoryAll.getPhotoPaths().get(0));
        }
        directories.add(INDEX_ALL_PHOTOS, photoDirectoryAll);
        return directories;
    }

}
