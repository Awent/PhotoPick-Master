package com.simaple;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public class PhotoPagerAnimaActivity extends AppCompatActivity {

    private int imageSize;
    private GridLayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pager_animat);
        final String url = "http://pic1.win4000.com/wallpaper/5/4fcec0606aaeb.jpg";
        final SimpleDraweeView image = findViewById(R.id.image);
        image.setImageURI(Uri.parse(url));
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoPagerConfig.Builder(PhotoPagerAnimaActivity.this)
                        .addSingleBigImageUrl(url)
                        .setSavaImage(true)
                        .setAnimation(true)             //要有移动效果，必须设置这两个参数
                        .setBeginView(image)            //要有移动效果，必须设置这两个参数
                        .build();
            }
        });
        RecyclerView recyclerView = findViewById(com.awen.photo.R.id.recyclerView);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        TestAdapter adapter = new TestAdapter();
        recyclerView.setAdapter(adapter);

        DisplayMetrics metrics = new DisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        this.imageSize = metrics.widthPixels / 3;
    }

    private class TestAdapter extends RecyclerView.Adapter {

        private ArrayList<String> list;

        private TestAdapter() {
            list = ImageProvider.getImageUrls();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(viewGroup.getContext());
            return new ViewHolder(simpleDraweeView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ((ViewHolder) viewHolder).setData(i);
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            SimpleDraweeView simpleDraweeView;

            private ViewHolder(View itemView) {
                super(itemView);
                simpleDraweeView = (SimpleDraweeView) itemView;
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(imageSize,imageSize);
                simpleDraweeView.setLayoutParams(params);
                simpleDraweeView.setOnClickListener(this);
            }

            private void setData(int position) {
                Uri uri = Uri.parse(list.get(position));
                //不设置.setResizeOptions(new ResizeOptions(width, width))会显示有问题
                ImageRequest imageRequest = ImageRequestBuilder
                        .newBuilderWithSource(uri)
                        .setLocalThumbnailPreviewsEnabled(true)
                        .setProgressiveRenderingEnabled(false)
                        .setResizeOptions(new ResizeOptions(imageSize, imageSize))
                        .build();
                DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(imageRequest)
                        .setOldController(simpleDraweeView.getController())
                        .build();
                simpleDraweeView.setController(draweeController);
            }

            @Override
            public void onClick(View view) {
                new PhotoPagerConfig.Builder(PhotoPagerAnimaActivity.this)
                        .setBigImageUrls(list)
                        .setSavaImage(true)
                        .setPosition(getAdapterPosition())
                        .setAnimation(true)             //要有移动效果，必须设置这两个参数
                        .setLayoutManager(layoutManager)//要有移动效果，必须设置这两个参数
                        .build();
            }
        }
    }

}
