package com.awen.photo.photopick.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.awen.photo.Awen;
import com.awen.photo.BaseActivity;
import com.awen.photo.FrescoImageLoader;
import com.awen.photo.R;
import com.awen.photo.photopick.bean.PhotoPagerBean;
import com.awen.photo.photopick.util.AppPathUtil;
import com.awen.photo.photopick.util.ImageUtils;
import com.awen.photo.photopick.util.PermissionUtil;
import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.awen.photo.photopick.widget.RoundProgressBarDrawable;
import com.awen.photo.photopick.widget.ScalePhotoView;
import com.awen.photo.photopick.widget.photodraweeview.OnViewTapListener;
import com.awen.photo.photopick.widget.photodraweeview.PhotoDraweeView;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.io.IOException;
import java.util.WeakHashMap;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import me.relex.circleindicator.CircleIndicator;

/**
 * 图片查看器<br>
 * 后期会继续添加视频播放
 * <p>
 * 你可重写以下方法：<br>
 * {@link #setCustomView(int)} <br>
 * {@link #init()}}<br>
 * {@link #getCustomView()}<br>
 * {@link #getCurrentBitmap()}<br>
 * {@link #setIndicatorVisibility(boolean)}<br>
 * {@link #onSingleClick()}<br>
 * {@link #onLongClick(View)}<br>
 * {@link #saveImage()}<br>
 * {@link #onPageSelected(int)} ()}<br>
 * </p>
 *
 * @author Homk-M <Awentljs@gmail.com>
 */
public class PhotoPagerActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnLongClickListener {
    private static final String TAG = PhotoPagerActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 100;
    private static final String STATE_POSITION = "STATE_POSITION";
    private ViewPager viewPager;
    private CircleIndicator indicator;
    protected PhotoPagerBean photoPagerBean;
    private OnViewTapListener onViewTapListener;
    private View.OnClickListener onClickListener;
    private String saveImageLocalPath;
    private boolean saveImage;
    //-start 防止viewpager的预加载，使得到的图片跟要保存的图片保持一致
    private WeakHashMap<Integer, ImageRequest> wkRequest;
    private WeakHashMap<Integer, Bitmap> wkBitmap;
    private WeakHashMap<Integer, Uri> wkLongBitmapUri;//主要是为了保存长图的时候用到
    protected int currentPosition;
    //-end

    private int screenWith, screenHeight;

    private FrameLayout rootLayout;
    private View customView;

    /**
     * 设置自定义view layout
     *
     * @param layoutId
     */
    protected void setCustomView(@LayoutRes int layoutId) {
        if (layoutId == -1) {
            return;
        }
        customView = LayoutInflater.from(this).inflate(layoutId, null);
        rootLayout.addView(customView);
        init();
    }

    /**
     * 获取自定义的view
     *
     * @return
     */
    protected View getCustomView() {
        return customView;
    }

    /**
     * 初始化,可以在这里findViewById和数据初始化工作<br>
     * findViewById要用getCustomView(),例如: <code>getCustomView().findViewById(R.id.button)</code>
     */
    protected void init() {
    }

    protected Bundle getBundle() {
        return getIntent().getBundleExtra(PhotoPagerConfig.EXTRA_USER_BUNDLE);
    }

    /**
     * 获取当前position的bitmap
     *
     * @return
     */
    protected Bitmap getCurrentBitmap() {
        if (wkBitmap != null && wkBitmap.containsKey(currentPosition)) {
            return wkBitmap.get(currentPosition);
        }
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOpenToolBar(false);
        Bundle bundle = getIntent().getBundleExtra(PhotoPagerConfig.EXTRA_PAGER_BUNDLE);
        photoPagerBean = bundle.getParcelable(PhotoPagerConfig.EXTRA_PAGER_BEAN);
        if (photoPagerBean == null) {
            onBackPressed();
            return;
        }
        saveImage = photoPagerBean.isSaveImage();
        saveImageLocalPath = photoPagerBean.getSaveImageLocalPath();

        rootLayout = new FrameLayout(this);
        rootLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        final View content = LayoutInflater.from(this).inflate(R.layout.activity_photo_detail_pager, rootLayout);
        setContentView(rootLayout);

        indicator = (CircleIndicator) content.findViewById(R.id.indicator);
        viewPager = (ViewPager) content.findViewById(R.id.pager);
        viewPager.setAdapter(new SamplePagerAdapter());
        setIndicatorVisibility(true);
        if (savedInstanceState != null) {
            photoPagerBean.setPagePosition(savedInstanceState.getInt(STATE_POSITION));
        }
        viewPager.setCurrentItem(photoPagerBean.getPagePosition());
        viewPager.addOnPageChangeListener(this);
        //图片单击的回调
        onViewTapListener = new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                onSingleClick();
            }
        };

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSingleClick();
            }
        };

        screenWith = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        setCustomView(-1);//设置用户自定义的view

        ScalePhotoView scalePhotoView = (ScalePhotoView) content.findViewById(R.id.scalePhotoView);
        scalePhotoView.setOpenDownAnimate(photoPagerBean.isOpenDownAnimate());
        if (photoPagerBean.isOpenDownAnimate()) {
            scalePhotoView.setOnViewTouchListener(new ScalePhotoView.onViewTouchListener() {
                @Override
                public void onFinish() {
                    onBackPressed();
                }

                @Override
                public void onMoving(float deltaX, float deltaY) {
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, viewPager.getCurrentItem());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 图片单击回调
     */
    protected boolean onSingleClick() {
        onBackPressed();
        return false;
    }

    /**
     * //图片长按回调
     */
    @Override
    public boolean onLongClick(View view) {
        saveImageDialog();
        return false;
    }

    /**
     * 保存图片到图库
     */
    protected void saveImage() {
        //以下操作会回调这两个方法:#startPermissionSDSuccess(), #startPermissionSDFaild()
        PermissionGen.needPermission(PhotoPagerActivity.this, REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 设置Indicator图片下标的可见状态
     *
     * @param visibility
     */
    protected void setIndicatorVisibility(boolean visibility) {
        if (photoPagerBean.getBigImgUrls().size() == 1 || !visibility) {
            indicator.setVisibility(View.GONE);
        } else {
            indicator.setViewPager(viewPager);
        }
    }

    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return photoPagerBean.getBigImgUrls() == null ? 0 : photoPagerBean.getBigImgUrls().size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            final FrameLayout parent = new FrameLayout(container.getContext());
            parent.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            final PhotoDraweeView mPhotoDraweeView = new PhotoDraweeView(container.getContext());
            mPhotoDraweeView.setOnViewTapListener(onViewTapListener);

            GenericDraweeHierarchy hierarchy = mPhotoDraweeView.getHierarchy();
            hierarchy.setProgressBarImage(new RoundProgressBarDrawable());//设置进度条
            hierarchy.setActualImageFocusPoint(new PointF(0.5f, 0.5f)); // 居中显示
//            hierarchy.setFailureImage(getResources().getDrawable(R.mipmap.failure_image));
            hierarchy.setRetryImage(getResources().getDrawable(R.mipmap.failure_image), ScalingUtils.ScaleType.FIT_CENTER);
            //大图,包括长图，gif
            final String bigImgUrl = photoPagerBean.getBigImgUrls().get(position);
            //小图，可在大图前进行展示
            String lowImgUrl = (photoPagerBean.getLowImgUrls() == null || photoPagerBean.getLowImgUrls().isEmpty()) ? "" : photoPagerBean.getLowImgUrls().get(position);
            final Uri uri = Uri.parse(bigImgUrl);
            final ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(false)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setTapToRetryEnabled(true)//点击重试
                    .setAutoPlayAnimations(true)//如果是gif，自动播放
                    .setImageRequest(request)//原图，大图
                    .setLowResImageRequest(ImageRequest.fromUri(lowImgUrl))//低分辨率的图
                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                            super.onFinalImageSet(id, imageInfo, animatable);
                            if (imageInfo == null) {
                                return;
                            }
                            View view = loadLargerLongPhoto(position, imageInfo, bigImgUrl, uri);
                            if (view != null) {//长图
                                view.setTag(position);
                                parent.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                mPhotoDraweeView.setVisibility(View.GONE);
                            } else {
                                mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                            }
                        }
                    })
                    .setOldController(mPhotoDraweeView.getController())
                    .build();
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<CloseableImage>>
                    dataSource = imagePipeline.fetchDecodedImage(request, null);
            dataSource.subscribe(new BaseBitmapDataSubscriber() {

                                     @Override
                                     public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                         // You can use the bitmap in only limited ways
                                         // No need to do any cleanup.
                                         if (bitmap != null) {
                                             if (saveImage) {
                                                 if (wkBitmap == null) {
                                                     wkBitmap = new WeakHashMap<>();
                                                 }
                                                 wkBitmap.put(position, bitmap);
                                                 //图片下载完成并且开启图片保存才给长按保存
                                                 mPhotoDraweeView.setOnLongClickListener(PhotoPagerActivity.this);
                                             }
                                         } else {//如果是gif，bitmap是null的
                                             final String fileName = bigImgUrl.substring(bigImgUrl.lastIndexOf("/") + 1, bigImgUrl.length());
                                             if (fileName.contains(".gif")) {//判断文件是不是gif格式的
                                                 if (saveImage) {
                                                     if (wkRequest == null) {
                                                         wkRequest = new WeakHashMap<>();
                                                     }
                                                     wkRequest.put(position, request);
                                                     mPhotoDraweeView.setOnLongClickListener(PhotoPagerActivity.this);
                                                 }
                                             }
                                         }
                                     }

                                     @Override
                                     public void onFailureImpl(DataSource dataSource) {
                                         // No cleanup required here.
                                     }
                                 },
                    CallerThreadExecutor.getInstance());
            mPhotoDraweeView.setController(controller);
            parent.addView(mPhotoDraweeView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//            container.addView(subsamplingScaleImageView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            container.addView(parent);
            return parent;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (wkBitmap != null && wkBitmap.containsKey(position)) {
                wkBitmap.remove(position);
            }
            if (wkRequest != null && wkRequest.containsKey(position)) {
                wkRequest.remove(position);
            }
            if (photoPagerBean != null && photoPagerBean.getBigImgUrls().size() > 0 && position < photoPagerBean.getBigImgUrls().size()) {
                View view = ((FrameLayout) object).findViewWithTag(position);
                if (view != null && view instanceof SubsamplingScaleImageView) {
                    SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) view;
                    imageView.recycle();
                }
                FrescoImageLoader.evictFromMemoryCache(photoPagerBean.getBigImgUrls().get(position));
            }
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        private View currentView;

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            currentView = (View) object;
        }

        public View getPrimaryItem() {
            return currentView;
        }
    }

    private SubsamplingScaleImageView loadLargerLongPhoto(int position, ImageInfo imageInfo, String url, Uri uri) {
        String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
        if (fileName.contains(".webp") || fileName.contains(".gif")) {
            return null;
        }
        float offsetW = (imageInfo.getWidth() / imageInfo.getHeight()) - (screenWith / screenHeight);
        float offsetH = (imageInfo.getHeight() / imageInfo.getWidth()) - (screenHeight / screenWith);
        if (offsetW > 1.0f) {//横向长图
            return loadLongPhoto(position, 0, screenHeight / imageInfo.getHeight(), imageInfo, uri);
        } else if (offsetH > 0.8f) {//纵向长图
            return loadLongPhoto(position, 1, imageInfo, uri);
        }
        return null;
    }

    /**
     * 加载超长图
     */
    private SubsamplingScaleImageView loadLongPhoto(int position, int orientation, ImageInfo imageInfo, Uri uri) {
        return loadLongPhoto(position, orientation, 0, imageInfo, uri);
    }

    /**
     * 加载超长图
     */
    private SubsamplingScaleImageView loadLongPhoto(int position, int orientation, float hScale, ImageInfo imageInfo, Uri uri) {
        if (wkBitmap == null || !wkBitmap.containsKey(position)) {
            return null;
        }
        SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(this);
        Bitmap bitmap = wkBitmap.get(position);
        if (bitmap != null) {
            if (orientation == 1) {//纵向图
                //这里直接获取File，因为fresco得到的长图bitmap是经过牙刷处理的，质量太低
                File file = FrescoImageLoader.getCache(this, uri);
                ImageSource imageSource;
                if (file != null) {
                    imageSource = ImageSource.uri(file.getAbsolutePath());
                } else {
                    imageSource = ImageSource.bitmap(wkBitmap.get(position));
                }
                imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                imageView.setImage(imageSource, new ImageViewState(hScale, new PointF(0, 0), SubsamplingScaleImageView.ORIENTATION_0));
            } else {
                imageView.setMaxScale(hScale);
                imageView.setImage(ImageSource.bitmap(bitmap));
            }
            imageView.setOnClickListener(onClickListener);
            //图片下载完成并且开启图片保存才给长按保存
            imageView.setOnLongClickListener(this);
            if (wkLongBitmapUri == null) {
                wkLongBitmapUri = new WeakHashMap<>();
            }
            wkLongBitmapUri.put(position, uri);
        } else {
            imageView.setImage(ImageSource.resource(R.mipmap.failure_image));
        }
        return imageView;
    }

    private Uri getUri(String url) {
        Log.e(TAG, "url = " + url);
        Uri uri;
        if (url.contains("file://")) {
            uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_FILE_SCHEME)
                    .path(url)
                    .build();
        } else if (url.contains("res://")) {
            uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                    .path(url)
                    .build();
        } else if (url.contains("asset://")) {
            uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_ASSET_SCHEME)
                    .path(url)
                    .build();
        } else {
            uri = Uri.parse(url);
        }
        return uri;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = REQUEST_CODE)
    private void startPermissionSDSuccess() {//获取读写sd卡权限成功回调
//        Log.e(TAG, "sd card image path = " + (saveImageLocalPath == null ? AppPathUtil.getBigBitmapCachePath() : saveImageLocalPath));
        //保存图片到本地
        String bigImgUrl = photoPagerBean.getBigImgUrls().get(currentPosition);
        String fileName = bigImgUrl.substring(bigImgUrl.lastIndexOf("/") + 1, bigImgUrl.length());
        if (!fileName.contains(".jpg")
                && !fileName.contains(".png")
                && !fileName.contains(".jpeg")
                && !fileName.contains(".gif")
                && !fileName.contains(".webp")) {
            //防止有些图片没有后缀名
            fileName = fileName + ".jpg";
        }
        String filePath = (Awen.getSaveImageLocalPath() == null ? AppPathUtil.getBigBitmapCachePath() : Awen.getSaveImageLocalPath()) + fileName;
        if (saveImageLocalPath != null) {
            filePath = saveImageLocalPath + fileName;
        }
//        Log.e(TAG, "save image fileName = " + fileName);
//        Log.e(TAG, "save image path = " + filePath);
        boolean state = false;
        if (fileName.contains(".gif")) {//保存gif图片
            if (wkRequest.containsKey(currentPosition)) {
                state = saveGif(filePath, wkRequest.get(currentPosition));
            }
        } else {//jpg
            if (wkBitmap.containsKey(currentPosition)) {
                state = saveImage(filePath, wkBitmap.get(currentPosition));
            }
        }
        String tips = state ? getString(R.string.save_image_aready, filePath) : getString(R.string.saved_faild);
        Toast.makeText(PhotoPagerActivity.this, tips, Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = REQUEST_CODE)
    private void startPermissionSDFaild() {
        new android.app.AlertDialog.Builder(this)
                .setMessage(getString(R.string.permission_tip_SD))
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PermissionUtil.startSystemSettingActivity(PhotoPagerActivity.this);
            }
        }).setCancelable(false).show();
    }

    private void saveImageDialog() {
        new AlertDialog.Builder(this)
                .setItems(new String[]{getString(R.string.save_big_image)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveImage();
                    }
                }).show();
    }

    /**
     * 保存图片到本地sd卡
     */
    private boolean saveImage(String filePath, Bitmap bitmap) {
        if (wkLongBitmapUri.containsKey(currentPosition)) {//如果保存的是长图
            File file = FrescoImageLoader.getCache(this, wkLongBitmapUri.get(currentPosition));
            if (file != null) {
                return ImageUtils.saveImageToGallery(filePath, file.getAbsolutePath());
            }
        }
        if (bitmap == null) {
            Toast.makeText(PhotoPagerActivity.this, R.string.saved_faild, Toast.LENGTH_SHORT).show();
            return false;
        }
        return ImageUtils.saveImageToGallery(filePath, bitmap);
    }

    /**
     * 保存gif到本地
     */
    private boolean saveGif(String filePath, ImageRequest request) {
        boolean state = false;
        //通过ImageRquest对象获取到对应的gif字节数组
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(request, null);
        BinaryResource bRes = ImagePipelineFactory.getInstance()
                .getMainFileCache()
                .getResource(cacheKey);
        try {
            byte[] b = bRes.read();
            if (b != null) {
                state = ImageUtils.saveImageToGallery(filePath, b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return state;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wkBitmap != null) {
            wkBitmap.clear();
            wkBitmap = null;
        }
        if (wkRequest != null) {
            wkRequest.clear();
            wkRequest = null;
        }
        if (wkLongBitmapUri != null) {
            wkLongBitmapUri.clear();
            wkLongBitmapUri = null;
        }
        onViewTapListener = null;
        onClickListener = null;
        if (viewPager != null) {
            viewPager.removeOnPageChangeListener(this);
            viewPager.setAdapter(null);
            viewPager = null;
        }
        photoPagerBean = null;
    }

    @Override
    public void onBackPressed() {
        indicator.setVisibility(View.GONE);
        finish();
        overridePendingTransition(0, R.anim.image_pager_exit_animation);
        super.onBackPressed();
    }

}
