package com.awen.photo.photopick.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.awen.photo.FrescoImageLoader;
import com.awen.photo.R;
import com.awen.photo.photopick.bean.Photo;
import com.awen.photo.photopick.bean.PhotoPickBean;
import com.awen.photo.photopick.controller.PhotoPickConfig;
import com.awen.photo.photopick.controller.PhotoPreviewConfig;
import com.awen.photo.photopick.ui.ClipPictureActivity;
import com.awen.photo.photopick.ui.PhotoPickActivity;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class PhotoPickAdapter extends RecyclerView.Adapter {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    public static ArrayList<Photo> photos;//图库
    public static ArrayList<String> selectPhotos;//已选择了的图片
    public static ArrayList<Photo> previewPhotos;//要预览选择了的图片
    private int imageSize;
    private PhotoPickBean pickBean;

    public PhotoPickAdapter(Context context, PhotoPickBean pickBean) {
        this.context = context;
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        this.imageSize = metrics.widthPixels / pickBean.getSpanCount();
        this.pickBean = pickBean;
        if (photos == null) {
            photos = new ArrayList<>();
        }
        if (selectPhotos == null) {
            selectPhotos = new ArrayList<>();
        }
        if (previewPhotos == null) {
            previewPhotos = new ArrayList<>();
        }
    }

    public void refresh(List<Photo> photos) {
        PhotoPickAdapter.photos.clear();
        PhotoPickAdapter.photos.addAll(photos);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_pick, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setData(position);
    }

    @Override
    public int getItemCount() {
        return pickBean.isShowCamera() ? (photos == null ? 0 : photos.size() + 1) : (photos == null ? 0 : photos.size());
    }

    private Photo getItem(int position) {
        return pickBean.isShowCamera() ? photos.get(position - 1) : photos.get(position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView imageView;
        private CheckBox checkbox;
        private ImageView gifIcon;

        ViewHolder(View itemView) {
            super(itemView);
            gifIcon = itemView.findViewById(R.id.gifIcon);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.imageView);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            imageView.getLayoutParams().height = imageSize;
            imageView.getLayoutParams().width = imageSize;
            checkbox.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void setData(int position) {
            Uri uri;
            if (pickBean.isShowCamera() && position == 0) {
                checkbox.setVisibility(View.GONE);
                gifIcon.setVisibility(View.GONE);
                uri = new Uri.Builder()
                        .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                        .path(String.valueOf(R.mipmap.take_photo))
                        .build();
            } else {
                Photo photo = getItem(position);
                gifIcon.setVisibility(photo.isGif() ? View.VISIBLE : View.GONE);
                if (pickBean.isClipPhoto()) {
                    checkbox.setVisibility(View.GONE);
                } else {
                    checkbox.setVisibility(View.VISIBLE);
                    checkbox.setChecked(selectPhotos.contains(photo.getPath()));
                }
                uri = new Uri.Builder()
                        .scheme(UriUtil.LOCAL_FILE_SCHEME)
                        .path(photo.getPath())
                        .build();
            }
            //不设置.setResizeOptions(new ResizeOptions(width, width))会显示有问题
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setProgressiveRenderingEnabled(false)
                    .setResizeOptions(new ResizeOptions(imageSize, imageSize))
                    .build();
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .setOldController(imageView.getController())
                    .build();
            imageView.setController(draweeController);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == R.id.checkbox) {
                if (selectPhotos.contains(getItem(position).getPath())) {
                    checkbox.setChecked(false);
                    selectPhotos.remove(getItem(position).getPath());
                    previewPhotos.remove(getItem(position));
                } else {
                    if (selectPhotos.size() == pickBean.getMaxPickSize()) {
                        checkbox.setChecked(false);
                        return;
                    } else {
                        checkbox.setChecked(true);
                        selectPhotos.add(getItem(position).getPath());
                        previewPhotos.add(getItem(position));
                    }
                }
                if (onUpdateListener != null) {
                    onUpdateListener.updataToolBarTitle(getTitle());
                }
            } else if (v.getId() == R.id.photo_pick_rl) {
                if (pickBean.isShowCamera() && position == 0) {
                    //以下操作会回调Activity中的#selectPicFromCameraSuccess()或selectPicFromCameraFailed()
                    PermissionGen.needPermission((Activity) context, PhotoPickActivity.REQUEST_CODE_PERMISSION_CAMERA, Manifest.permission.CAMERA);
                } else if (pickBean.isClipPhoto()) {//头像裁剪
                    startClipPic(getItem(position).getPath());
                } else {//查看大图
                    new PhotoPreviewConfig.Builder((Activity) context)
                            .setPosition(pickBean.isShowCamera() ? position - 1 : position)
                            .setMaxPickSize(pickBean.getMaxPickSize())
                            .setOriginalPicture(pickBean.isOriginalPicture())
                            .build();
                }
            }
        }
    }

    public void startClipPic(String picPath) {
        Intent intent = new Intent(context, ClipPictureActivity.class);
        intent.putExtra(ClipPictureActivity.USER_PHOTO_PATH, picPath);
        ((Activity) context).startActivityForResult(intent, PhotoPickActivity.REQUEST_CODE_CLIPIC);
    }

    /**
     * 如果是多选title才会变化，要不然单选的没有变
     *
     * @return only work for {@link PhotoPickConfig#MODE_MULTIP_PICK}
     */
    public String getTitle() {
        String title = context.getString(R.string.select_photo);
        if (pickBean.getPickMode() == PhotoPickConfig.MODE_MULTIP_PICK && selectPhotos.size() >= 1) {//不是单选，更新title
            title = selectPhotos.size() + "/" + pickBean.getMaxPickSize();
        }
        return title;
    }

    /**
     * 获取已经选择了的图片
     *
     * @return selected photos
     */
    public ArrayList<String> getSelectPhotos() {
        return selectPhotos;
    }

    public static ArrayList<Photo> getPreviewPhotos() {
        ArrayList<Photo> list = new ArrayList<>();
        list.addAll(previewPhotos);
        return list;
    }

    private OnUpdateListener onUpdateListener;

    public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }

    public interface OnUpdateListener {
        void updataToolBarTitle(String title);
    }

    public void destroy() {
        photos.clear();
        selectPhotos.clear();
        previewPhotos.clear();
        photos = null;
        selectPhotos = null;
        previewPhotos = null;
    }
}
