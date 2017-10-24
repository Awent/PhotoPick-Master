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
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.awen.photo.Awen;
import com.awen.photo.BaseActivity;
import com.awen.photo.FrescoImageLoader;
import com.awen.photo.R;
import com.awen.photo.photopick.adapter.PhotoPickAdapter;
import com.awen.photo.photopick.bean.Photo;
import com.awen.photo.photopick.bean.PhotoPreviewBean;
import com.awen.photo.photopick.controller.PhotoPickConfig;
import com.awen.photo.photopick.controller.PhotoPreviewConfig;
import com.awen.photo.photopick.util.FileSizeUtil;
import com.awen.photo.photopick.util.ViewUtil;
import com.awen.photo.photopick.widget.HackyViewPager;
import com.awen.photo.photopick.widget.photodraweeview.OnViewTapListener;
import com.awen.photo.photopick.widget.photodraweeview.PhotoDraweeView;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
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
 * 图片预览,包括长图(跟微信微博一样),后期会继续添加视频预览
 * Created by Awen <Awentljs@gmail.com>
 */

public class PhotoPreviewActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private final String TAG = getClass().getSimpleName();
    private ArrayList<Photo> photos;
    private ArrayList<String> selectPhotos;
    private ArrayList<Photo> previewPhotos;
    private OnViewTapListener onViewTapListener;//图片单击
    private View.OnClickListener onClickListener;//长图单击
    private CheckBox checkbox;
    private RadioButton radioButton;
    private int pos;
    private int maxPickSize;
    private boolean isChecked = false;
    private boolean originalPicture;
    private int screenWith, screenHeight;
    private HackyViewPager viewPager;
    private LinearLayout bottom_ll;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        setOpenNavigationBar(true);
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
        photos = bean.isPreview() ? PhotoPickAdapter.getPreviewPhotos() : PhotoPickAdapter.photos;
        if (photos == null || photos.isEmpty()) {
            finish();
            return;
        }
        originalPicture = bean.isOriginalPicture();
        maxPickSize = bean.getMaxPickSize();
        selectPhotos = PhotoPickAdapter.selectPhotos;
        previewPhotos = PhotoPickAdapter.previewPhotos;
        final int beginPosition = bean.getPosition();
        setOpenToolBar(false);
        setContentView(R.layout.activity_photo_select);

        radioButton = (RadioButton) findViewById(R.id.radioButton);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        viewPager = (HackyViewPager) findViewById(R.id.pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Awen.getToolbarBackGround());
        toolbar.setTitle((beginPosition + 1) + "/" + photos.size());
        setSupportActionBar(toolbar);

        if (selectPhotos != null && selectPhotos.contains(photos.get(0).getPath())) {//pos=0的时候
            checkbox.setChecked(true);
        } else {
            checkbox.setChecked(false);
        }
        viewPager.addOnPageChangeListener(this);

        //图片单击的回调
        onViewTapListener = new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                onSingleClick();
            }
        };

        //长图单击回调
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSingleClick();
            }
        };

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPhotos == null) {
                    selectPhotos = new ArrayList<>();
                }
                if (previewPhotos == null) {
                    previewPhotos = new ArrayList<>();
                }
                String path = photos.get(pos).getPath();
                if (selectPhotos.contains(path)) {
                    selectPhotos.remove(path);
                    previewPhotos.remove(photos.get(pos));
                    checkbox.setChecked(false);
                } else {
                    if (maxPickSize == selectPhotos.size()) {
                        checkbox.setChecked(false);
                        return;
                    }
                    selectPhotos.add(path);
                    previewPhotos.add(photos.get(pos));
                    checkbox.setChecked(true);
                }
                updateMenuItemTitle();
            }
        });

        if (originalPicture) {
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
        } else {
            radioButton.setVisibility(View.GONE);
        }

        viewPager.setAdapter(new SamplePagerAdapter());
        viewPager.setCurrentItem(beginPosition);

        screenWith = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        //适配导航栏
        bottom_ll = (LinearLayout) findViewById(R.id.bottom_ll);
        int navigationBarHeight = ViewUtil.getNavigationBarHeight(this);
        if (navigationBarHeight > 0) {
            View navigation_bar_view = new View(this);
            navigation_bar_view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, navigationBarHeight));
            bottom_ll.addView(navigation_bar_view);
        }
    }

    private void onSingleClick() {
        if (toolBarStatus) {
            hideViews();
        } else {
            showViews();
        }
    }

    private void updateMenuItemTitle() {
        if (selectPhotos == null || selectPhotos.isEmpty()) {
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
        intent.putExtra(PhotoPreviewConfig.EXTRA_ORIGINAL_PIC, radioButton.isChecked());
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
        bottom_ll.animate().alpha(0.0f).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        toolBarStatus = true;
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        bottom_ll.animate().alpha(1.0f).setInterpolator(new DecelerateInterpolator(2));
    }

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
            if (pos == 1 && selectPhotos.contains(photos.get(pos - 1).getPath())) {
                checkbox.setChecked(true);
            }
        } else {
            checkbox.setChecked(false);
        }
        if (originalPicture) {
            radioButton.setText(getString(R.string.image_size, FileSizeUtil.formatFileSize(photos.get(pos).getSize())));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return photos == null ? 0 : photos.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            Photo photo = photos.get(position);
            final String bigImgUrl = photo.getPath();
            float offsetW = 0.0f, offsetH = 0.0f;
            if (photo.getWidth() > 0 && photo.getHeight() > 0) {
                offsetW = (photo.getWidth() / photo.getHeight()) - (screenWith / screenHeight);
                offsetH = (photo.getHeight() / photo.getWidth()) - (screenHeight / screenWith);
            }
//            Log.e(TAG,"offsetW = " + offsetW + ",offsetH = " + offsetH);
            if (offsetW > 1.0f && !photo.isGif() && !photo.isWebp()) {//横向长图
                photos.get(position).setLongPhoto(true);
                SubsamplingScaleImageView subsamplingScaleImageView = loadLongPhoto(new File(bigImgUrl), 0, screenHeight / photo.getHeight());
                container.addView(subsamplingScaleImageView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                return subsamplingScaleImageView;
            } else if (offsetH > 0.8f && !photo.isGif() && !photo.isWebp()) {//纵向长图
                photos.get(position).setLongPhoto(true);
                SubsamplingScaleImageView subsamplingScaleImageView = loadLongPhoto(new File(bigImgUrl), 1);
                container.addView(subsamplingScaleImageView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                return subsamplingScaleImageView;
            } else {
                //不是长图，gif等
                final PhotoDraweeView mPhotoDraweeView = new PhotoDraweeView(container.getContext());
                mPhotoDraweeView.setBackgroundColor(getResources().getColor(android.R.color.black));
                mPhotoDraweeView.setOnViewTapListener(onViewTapListener);

                GenericDraweeHierarchy hierarchy = mPhotoDraweeView.getHierarchy();
                hierarchy.setFailureImage(getResources().getDrawable(R.mipmap.failure_image), ScalingUtils.ScaleType.CENTER);
//            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER); // 修改缩放类型
//            hierarchy.setActualImageFocusPoint(new PointF(0.5f, 0.5f)); // 居中显示
                File file = new File(bigImgUrl);
                Uri uri = Uri.fromFile(file);
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
                                mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                            }
                        })
                        .setOldController(mPhotoDraweeView.getController())
                        .build();
                mPhotoDraweeView.setController(controller);
                container.addView(mPhotoDraweeView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                return mPhotoDraweeView;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (photos != null && photos.size() > 0 && position < photos.size()) {
                Photo photo = photos.get(position);
                if (photo.isLongPhoto()) {
                    SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) object;
                    if (imageView != null) {
                        imageView.recycle();
                    }
                }
                FrescoImageLoader.evictFromMemoryCache(photo.getPath());
            }
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    /**
     * gif播放与暂停
     */
    private void gifPlayController(DraweeController draweeController) {
        if (draweeController == null) {
            return;
        }
        Animatable animatable = draweeController.getAnimatable();
        if (animatable == null) {
            return;
        }
        //判断是否正在运行
        if (animatable.isRunning()) {
            //运行中，停止
            animatable.stop();
        } else {
            //停止了，运行
            animatable.start();
        }
    }

    /**
     * 加载超长图
     */
    private SubsamplingScaleImageView loadLongPhoto(File file, int orientation) {
        return loadLongPhoto(file, orientation, 0);
    }

    /**
     * 加载超长图
     */
    private SubsamplingScaleImageView loadLongPhoto(File file, int orientation, int hScale) {
        SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(this);
        imageView.setOnClickListener(onClickListener);
        imageView.setBackgroundColor(getResources().getColor(android.R.color.black));
        if (file != null && file.exists()) {
            if (orientation == 1) {//纵向图
                imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                imageView.setImage(ImageSource.uri(file.getAbsolutePath()), new ImageViewState(0, new PointF(0, 0), SubsamplingScaleImageView.ORIENTATION_0));
            } else {
                imageView.setMaxScale(hScale);
                imageView.setImage(ImageSource.uri(file.getAbsolutePath()));
            }
        } else {
            imageView.setImage(ImageSource.resource(R.mipmap.failure_image));
        }
        return imageView;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.image_pager_exit_animation);
    }

    @Override
    protected void onDestroy() {
        photos = null;
        selectPhotos = null;
        previewPhotos = null;
        onViewTapListener = null;
        onClickListener = null;
        if (viewPager != null) {
            viewPager.removeOnPageChangeListener(this);
            viewPager.setAdapter(null);
            viewPager = null;
        }
        super.onDestroy();
    }
}
