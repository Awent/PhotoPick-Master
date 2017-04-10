package com.awen.photo.photopick.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.awen.photo.R;
import com.awen.photo.photopick.bean.Photo;
import com.awen.photo.photopick.bean.PhotoPickBean;
import com.awen.photo.photopick.controller.PhotoPickConfig;
import com.awen.photo.photopick.controller.PhotoPreviewConfig;
import com.awen.photo.photopick.ui.ClipPictureActivity;
import com.awen.photo.photopick.ui.PhotoPickActivity;
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
    private ArrayList<Photo> photos = new ArrayList<>();
    private ArrayList<String> selectPhotos = new ArrayList<>();
    private int maxPickSize;//default values
    private int pickMode;
    private int imageSize;
    //    private int margin = 5;
    private boolean showCamera;
    private boolean isClipPhoto;
    private boolean isOriginalPicture;//是否让用户选择原图
    private Uri cameraUri;

    public PhotoPickAdapter(Context context, PhotoPickBean pickBean) {
        this.context = context;
//        this.width = metrics.widthPixels / spanCount - (spanCount + 1) * margin;
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        this.imageSize = metrics.widthPixels / pickBean.getSpanCount();
        this.pickMode = pickBean.getPickMode();
        this.maxPickSize = pickBean.getMaxPickSize();
        this.showCamera = pickBean.isShowCamera();
        this.isClipPhoto = pickBean.isClipPhoto();
        this.isOriginalPicture = pickBean.isOriginalPicture();
    }

    public void refresh(List<Photo> photos) {
        this.photos.clear();
        this.photos.addAll(photos);
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
        return showCamera ? (photos == null ? 0 : photos.size() + 1) : (photos == null ? 0 : photos.size());
    }

    private Photo getItem(int position) {
        return showCamera ? photos.get(position - 1) : photos.get(position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView imageView;
        private CheckBox checkbox;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.imageView);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            imageView.getLayoutParams().height = imageSize;
            imageView.getLayoutParams().width = imageSize;
            checkbox.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void setData(int position) {
            String url;
            Uri uri;
            if (showCamera && position == 0) {
                checkbox.setVisibility(View.GONE);
                url = "res:///" + R.mipmap.take_photo;
                uri = Uri.parse(url);
            } else {
                Photo photo = getItem(position);
                if (isClipPhoto) {
                    checkbox.setVisibility(View.GONE);
                } else {
                    checkbox.setVisibility(View.VISIBLE);
                    checkbox.setChecked(selectPhotos.contains(photo.getPath()));
                }
                url = photo.getPath();
                uri = Uri.fromFile(new File(url));
            }
            //不设置.setResizeOptions(new ResizeOptions(width, width))会显示有问题
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setResizeOptions(new ResizeOptions(imageSize, imageSize))
                    .build();
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .setAutoPlayAnimations(true)
                    .setOldController(imageView.getController())
                    .build();
            imageView.setController(draweeController);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(v.getId() == R.id.checkbox){
                if (selectPhotos.contains(getItem(position).getPath())) {
                    checkbox.setChecked(false);
                    selectPhotos.remove(getItem(position).getPath());
                } else {
                    if (selectPhotos.size() == maxPickSize) {
                        checkbox.setChecked(false);
                        return;
                    } else {
                        checkbox.setChecked(true);
                        selectPhotos.add(getItem(position).getPath());
                    }
                }
                if (onUpdateListener != null) {
                    onUpdateListener.updataToolBarTitle(getTitle());
                }
            }else if(v.getId() == R.id.photo_pick_rl){
                if (showCamera && position == 0) {
                    //以下操作会回调Activity中的#selectPicFromCameraSuccess()或selectPicFromCameraFailed()
                    PermissionGen.needPermission((Activity) context, PhotoPickActivity.REQUEST_CODE_PERMISSION_CAMERA, Manifest.permission.CAMERA);
                } else if (isClipPhoto) {//头像裁剪
                    startClipPic(getItem(position).getPath());
                } else {//查看大图
                    new PhotoPreviewConfig.Builder((Activity) context)
                            .setPosition(showCamera ? position - 1 : position)
                            .setMaxPickSize(maxPickSize)
                            .setPhotos(photos)
                            .setSelectPhotos(selectPhotos)
                            .setOriginalPicture(isOriginalPicture)
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
     * 启动Camera拍照
     */
    public void selectPicFromCamera() {
        if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, R.string.cannot_take_pic, Toast.LENGTH_SHORT).show();
            return;
        }
        // 直接将拍到的照片存到手机默认的文件夹
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        cameraUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        ((Activity) context).startActivityForResult(intent, PhotoPickActivity.REQUEST_CODE_CAMERA);
    }

    public Uri getCameraUri() {
        return cameraUri;
    }

    /**
     * 如果是多选title才会变化，要不然单选的没有变
     *
     * @return only work for {@link PhotoPickConfig#MODE_MULTIP_PICK}
     */
    public String getTitle() {
        String title = context.getString(R.string.select_photo);
        if (pickMode == PhotoPickConfig.MODE_MULTIP_PICK && selectPhotos.size() >= 1) {//不是单选，更新title
            title = selectPhotos.size() + "/" + maxPickSize;
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

    private OnUpdateListener onUpdateListener;

    public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }

    public interface OnUpdateListener {
        void updataToolBarTitle(String title);
    }
}
