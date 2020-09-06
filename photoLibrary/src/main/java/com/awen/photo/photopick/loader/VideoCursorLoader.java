package com.awen.photo.photopick.loader;

import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class VideoCursorLoader {

    @NonNull
    private Uri uri;
    @Nullable
    private String[] projection;
    @Nullable
    private String selection;
    @Nullable
    private String[] selectionArgs;
    @Nullable
    private String sortOrder;

    public VideoCursorLoader() {
        String[] PROJECTION = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.DURATION,
        };

        //default ，默认配置
        setUri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        setProjection(PROJECTION);
        setSelectionArgs(selectionArgs);
        setSortOrder(MediaStore.Video.Media.DATE_ADDED + " DESC");
    }

    public VideoCursorLoader(@NonNull Uri uri, @Nullable String[] projection,
                             @Nullable String selection, @Nullable String[] selectionArgs,
                             @Nullable String sortOrder) {
        this.uri = uri;
        this.projection = projection;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.sortOrder = sortOrder;
    }

    @NonNull
    public Uri getUri() {
        return uri;
    }

    public void setUri(@NonNull Uri uri) {
        this.uri = uri;
    }

    @Nullable
    public String[] getProjection() {
        return projection;
    }

    public void setProjection(@Nullable String[] projection) {
        this.projection = projection;
    }

    @Nullable
    public String getSelection() {
        return selection;
    }

    public void setSelection(@Nullable String selection) {
        this.selection = selection;
    }

    @Nullable
    public String[] getSelectionArgs() {
        return selectionArgs;
    }

    public void setSelectionArgs(@Nullable String[] selectionArgs) {
        this.selectionArgs = selectionArgs;
    }

    @Nullable
    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(@Nullable String sortOrder) {
        this.sortOrder = sortOrder;
    }

}
