package com.awen.photo.photopick.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.awen.photo.R;
import com.awen.photo.photopick.bean.Photo;
import com.awen.photo.photopick.bean.PhotoDirectory;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class PhotoGalleryAdapter extends RecyclerView.Adapter {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private int selected;
    private List<PhotoDirectory> directories = new ArrayList<>();
    private int imageSize;

    public PhotoGalleryAdapter(Context context) {
        this.context = context;
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        this.imageSize = metrics.widthPixels / 6;
    }

    public void refresh(List<PhotoDirectory> directories) {
        this.directories.clear();
        this.directories.addAll(directories);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_gallery, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setData(getItem(position), position);
    }

    @Override
    public int getItemCount() {
        return directories.size();
    }

    public PhotoDirectory getItem(int position) {
        return this.directories.get(position);
    }

    public void changeSelect(int position) {
        this.selected = position;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView imageView;
        private ImageView photo_gallery_select;
        private TextView name, num;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.imageView);
            name = (TextView) itemView.findViewById(R.id.name);
            num = (TextView) itemView.findViewById(R.id.num);
            photo_gallery_select = (ImageView) itemView.findViewById(R.id.photo_gallery_select);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imageSize, imageSize);
            imageView.setLayoutParams(params);
            itemView.setOnClickListener(this);
        }

        public void setData(PhotoDirectory directory, int position) {
            itemView.setTag(position);
            if (directory == null || directory.getCoverPath() == null) {
                return;
            }
            if (selected == position) {
                photo_gallery_select.setImageResource(R.mipmap.select_icon);
            } else {
                photo_gallery_select.setImageBitmap(null);
            }
            name.setText(directory.getName());
            num.setText(context.getString(R.string.gallery_num, String.valueOf(directory.getPhotoPaths().size())));
            //不设置.setResizeOptions(new ResizeOptions(imageSize, imageSize))会显示有问题
            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.fromFile(new File(directory.getCoverPath())))
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setResizeOptions(new ResizeOptions(imageSize, imageSize)).build();
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .setAutoPlayAnimations(true)
                    .setOldController(imageView.getController())
                    .build();
            imageView.setController(draweeController);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.photo_gallery_rl:
                    if (onItemClickListener != null) {
                        int position = (int) v.getTag();
                        changeSelect(position);
                        onItemClickListener.onClick(getItem(position).getPhotos());
                    }
                    break;
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(List<Photo> photos);
    }
}
