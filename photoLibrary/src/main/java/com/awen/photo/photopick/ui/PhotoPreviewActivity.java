package com.awen.photo.photopick.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.awen.photo.Awen;
import com.awen.photo.BaseActivity;
import com.awen.photo.R;
import com.awen.photo.photopick.bean.Photo;
import com.awen.photo.photopick.bean.PhotoPreviewBean;
import com.awen.photo.photopick.controller.PhotoPickConfig;
import com.awen.photo.photopick.controller.PhotoPreviewConfig;
import com.awen.photo.photopick.util.FileSizeUtil;
import com.awen.photo.photopick.widget.HackyViewPager;
import com.awen.photo.photopick.widget.photodraweeview.OnViewTapListener;
import com.awen.photo.photopick.widget.photodraweeview.PhotoDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.util.ArrayList;

/**
 * 图片预览,后期会继续添加视频预览
 * Created by Awen <Awentljs@gmail.com>
 */

public class PhotoPreviewActivity extends BaseActivity {

    private ArrayList<Photo> photos;
    private ArrayList<String> selectPhotos;
    private OnViewTapListener onViewTapListener;
    private CheckBox checkbox;
    private RadioButton radioButton;
    private int pos;
    private int maxPickSize;
    private boolean isChecked = false;
    private boolean originalPicture;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        Bundle bundle = getIntent().getBundleExtra(PhotoPreviewConfig.EXTRA_BUNDLE);
        if (bundle == null) {
            throw new NullPointerException("bundle is null,please init it");
        }
        PhotoPreviewBean bean = bundle.getParcelable(PhotoPreviewConfig.EXTRA_BEAN);
        if (bean == null) {
            finish();
            return;
        }
        photos = bean.getPhotos();
        if (photos == null || photos.isEmpty()) {
            finish();
            return;
        }
        originalPicture = bean.isOriginalPicture();
        maxPickSize = bean.getMaxPickSize();
        selectPhotos = bean.getSelectPhotos();
        final int beginPosition = bean.getPosition();
        setOpenToolBar(false);
        setContentView(R.layout.activity_photo_select);

        radioButton = (RadioButton) findViewById(R.id.radioButton);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        HackyViewPager viewPager = (HackyViewPager) findViewById(R.id.pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Awen.getToolbarBackGround());
        toolbar.setTitle((beginPosition + 1) + "/" + photos.size());
        setSupportActionBar(toolbar);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
                position++;
                toolbar.setTitle(position + "/" + photos.size());
                if (selectPhotos != null && selectPhotos.contains(photos.get(pos).getPath())) {
                    checkbox.setChecked(true);
                    if(pos == 1 && selectPhotos.contains(photos.get(pos - 1).getPath())){
                        checkbox.setChecked(true);
                    }
                } else {
                    checkbox.setChecked(false);
                }
                if(originalPicture){
                    radioButton.setText(getString(R.string.image_size, FileSizeUtil.formatFileSize(photos.get(pos).getSize())));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //图片单击的回调
        onViewTapListener = new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (toolBarStatus) {
                    hideViews();
                } else {
                    showViews();
                }
            }
        };

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPhotos == null) {
                    selectPhotos = new ArrayList<>();
                }
                String path = photos.get(pos).getPath();
                if (selectPhotos.contains(path)) {
                    selectPhotos.remove(path);
                    checkbox.setChecked(false);
                } else {
                    if (maxPickSize == selectPhotos.size()) {
                        checkbox.setChecked(false);
                        return;
                    }
                    selectPhotos.add(path);
                    checkbox.setChecked(true);
                }
                updateMenuItemTitle();
            }
        });

        if(originalPicture){
            radioButton.setText(getString(R.string.image_size, FileSizeUtil.formatFileSize(photos.get(beginPosition).getSize())));
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isChecked) {
                        radioButton.setChecked(false);
                        isChecked = false;
                    } else {
                        radioButton.setChecked(true);
                        isChecked = true;
                    }
                }
            });
        }else {
            radioButton.setVisibility(View.GONE);
        }

        viewPager.setAdapter(new SamplePagerAdapter());
        viewPager.setCurrentItem(beginPosition);
    }

    private void updateMenuItemTitle() {
        if (selectPhotos.isEmpty()) {
            menuItem.setTitle(R.string.send);
        } else {
            menuItem.setTitle(getString(R.string.sends, String.valueOf(selectPhotos.size()), String.valueOf(maxPickSize)));
        }
    }

    private MenuItem menuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        menuItem = menu.findItem(R.id.ok);
        updateMenuItemTitle();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ok && !selectPhotos.isEmpty()) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(PhotoPickConfig.EXTRA_STRING_ARRAYLIST, selectPhotos);
            setResult(Activity.RESULT_OK, intent);
            finish();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            backTo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void backTo() {
        Intent intent = new Intent();
        intent.putExtra("isBackPressed", true);
        intent.putStringArrayListExtra(PhotoPickConfig.EXTRA_STRING_ARRAYLIST, selectPhotos);
        intent.putExtra(PhotoPreviewConfig.EXTRA_ORIGINAL_PIC,originalPicture);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backTo();
        super.onBackPressed();
    }

    private boolean toolBarStatus = true;

    private void hideViews() {//隐藏toolbar
        toolBarStatus = false;
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        toolBarStatus = true;
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return photos.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            final PhotoDraweeView mPhotoDraweeView = new PhotoDraweeView(container.getContext());
            mPhotoDraweeView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mPhotoDraweeView.setOnViewTapListener(onViewTapListener);

            GenericDraweeHierarchy hierarchy = mPhotoDraweeView.getHierarchy();
            hierarchy.setActualImageFocusPoint(new PointF(0.5f, 0.5f)); // 居中显示
//            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE); // 修改缩放类型

            String bigImgUrl = photos.get(position).getPath();
            Uri uri = Uri.fromFile(new File(bigImgUrl));
            ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true)
                    .setImageRequest(request)//原图，大图
                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                            super.onFinalImageSet(id, imageInfo, animatable);
                            if (imageInfo == null) {
                                return;
                            }
                            Log.e("PhotoSelectActivity", "imageWidth = " + imageInfo.getWidth() + ", imageHeight = " + imageInfo.getHeight());
                            mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                        }
                    })
                    .setOldController(mPhotoDraweeView.getController())
                    .build();
            mPhotoDraweeView.setController(controller);
            container.addView(mPhotoDraweeView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            return mPhotoDraweeView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.image_pager_exit_animation);
    }

}
