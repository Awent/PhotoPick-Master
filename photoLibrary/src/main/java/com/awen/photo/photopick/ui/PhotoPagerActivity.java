package com.awen.photo.photopick.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.awen.photo.Awen;
import com.awen.photo.BaseActivity;
import com.awen.photo.R;
import com.awen.photo.photopick.bean.PhotoPagerBean;
import com.awen.photo.photopick.util.AppPathUtil;
import com.awen.photo.photopick.util.ImageUtils;
import com.awen.photo.photopick.util.PermissionUtil;
import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.awen.photo.photopick.widget.HackyViewPager;
import com.awen.photo.photopick.widget.RoundProgressBar;
import com.awen.photo.photopick.widget.photodraweeview.OnViewTapListener;
import com.awen.photo.photopick.widget.photodraweeview.PhotoDraweeView;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
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
 *
 * @author Homk-M <Awentljs@gmail.com>
 */
public class PhotoPagerActivity extends BaseActivity {
    private static final String TAG = PhotoPagerActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 100;
    private static final String STATE_POSITION = "STATE_POSITION";
    private HackyViewPager viewPager;
    private PhotoPagerBean photoPagerBean;
    private OnViewTapListener onViewTapListener;
    private View.OnLongClickListener onLongClickListener;
    private String saveImageLocalPath;
    private boolean saveImage;
    //-start 防止viewpager的预加载，使得到的图片跟要保存的图片保持一致
    private WeakHashMap<Integer, ImageRequest> wkRequest;
    private WeakHashMap<Integer, Bitmap> wkBitmap;
    private int currentPosition;
    //-end

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOpenToolBar(false);
        setContentView(R.layout.activity_photo_detail_pager);
        Bundle bundle = getIntent().getBundleExtra(PhotoPagerConfig.EXTRA_PAGER_BUNDLE);
        photoPagerBean = bundle.getParcelable(PhotoPagerConfig.EXTRA_PAGER_BEAN);
        if (photoPagerBean == null) {
            finish();
            return;
        }
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager = (HackyViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SamplePagerAdapter());
        if (photoPagerBean.getBigImgUrls().size() == 1) {
            // if the urls has a single image,to set indicator not visible
            indicator.setVisibility(View.GONE);
        } else {
            indicator.setViewPager(viewPager);
        }
        if (savedInstanceState != null) {
            photoPagerBean.setPagePosition(savedInstanceState.getInt(STATE_POSITION));
        }
        viewPager.setCurrentItem(photoPagerBean.getPagePosition());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        });
        //图片单击的回调
        onViewTapListener = new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                finish();
            }
        };

        //图片长按回调
        onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveImageDialog();
                return true;
            }
        };

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, viewPager.getCurrentItem());
    }

    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return photoPagerBean.getBigImgUrls() == null ? 0 : photoPagerBean.getBigImgUrls().size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            final PhotoDraweeView mPhotoDraweeView = new PhotoDraweeView(container.getContext());
            mPhotoDraweeView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mPhotoDraweeView.setOnViewTapListener(onViewTapListener);

            GenericDraweeHierarchy hierarchy = mPhotoDraweeView.getHierarchy();
            hierarchy.setProgressBarImage(new RoundProgressBar());//设置进度条
            hierarchy.setActualImageFocusPoint(new PointF(0.5f, 0.5f)); // 居中显示
//            hierarchy.setFailureImage(getResources().getDrawable(R.mipmap.failure_image));
            hierarchy.setRetryImage(getResources().getDrawable(R.mipmap.failure_image));

            final String bigImgUrl = photoPagerBean.getBigImgUrls().get(position);
            saveImageLocalPath = photoPagerBean.getSaveImageLocalPath();
            //小图，可在大图前进行展示
            String lowImgUrl = (photoPagerBean.getLowImgUrls() == null || photoPagerBean.getLowImgUrls().isEmpty()) ? "" : photoPagerBean.getLowImgUrls().get(position);
            saveImage = photoPagerBean.isSaveImage();

            Uri uri;
            if (bigImgUrl.contains("/storage/") || bigImgUrl.contains("file://") || bigImgUrl.contains("/emulated/") || bigImgUrl.contains("#")) {
                //图片路径带有"#"符号的显示不出这张图片来,我已报告给fb,已确认是bug,不知道现在修复了没有
                uri = Uri.fromFile(new File(bigImgUrl));
                Log.e(TAG, "file path = " + bigImgUrl);
            } else {
                uri = Uri.parse(bigImgUrl);
            }
            final ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)
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
                                Log.e(TAG, "imageInfo = null");
                                return;
                            }
                            mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
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
                                                 mPhotoDraweeView.setOnLongClickListener(onLongClickListener);
                                             }
                                         } else {//如果是gif，bitmap是null的
                                             final String fileName = bigImgUrl.substring(bigImgUrl.lastIndexOf("/") + 1, bigImgUrl.length());
                                             if (fileName.contains(".gif")) {//判断文件是不是gif格式的
                                                 if (saveImage) {
                                                     if (wkRequest == null) {
                                                         wkRequest = new WeakHashMap<>();
                                                     }
                                                     wkRequest.put(position, request);
                                                     mPhotoDraweeView.setOnLongClickListener(onLongClickListener);
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
            container.addView(mPhotoDraweeView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            return mPhotoDraweeView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (wkBitmap != null && wkBitmap.containsKey(position)) {
                wkBitmap.remove(position);
            }
            if (wkRequest != null && wkRequest.containsKey(position)) {
                wkRequest.remove(position);
            }
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e(TAG, "notifyPermissionsChange");
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = REQUEST_CODE)
    private void startPermissionSDSuccess() {//获取读写sd卡权限成功回调
        Log.e(TAG, "sd card image path = " + (saveImageLocalPath == null ? AppPathUtil.getBigBitmapCachePath() : saveImageLocalPath));
        //保存图片到本地
        String bigImgUrl = photoPagerBean.getBigImgUrls().get(currentPosition);
        String fileName = bigImgUrl.substring(bigImgUrl.lastIndexOf("/") + 1, bigImgUrl.length());
        if (!fileName.contains(".jpg") && !fileName.contains(".png") && !fileName.contains(".jpeg") && !fileName.contains(".gif")) {
            //防止有些图片没有后缀名
            fileName = fileName + ".jpg";
        }
        String filePath = (Awen.getSaveImageLocalPath() == null ? AppPathUtil.getBigBitmapCachePath() : Awen.getSaveImageLocalPath()) + fileName;
        if(saveImageLocalPath != null){
            filePath = saveImageLocalPath + fileName;
        }
        Log.e(TAG, "save image fileName = " + fileName);
        Log.e(TAG, "save image path = " + filePath);
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
        Log.e(TAG, "startPermissionSuccess");
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
        Log.e(TAG, "startPermissionFaild");
    }

    private void saveImageDialog() {
        new AlertDialog.Builder(this)
                .setItems(new String[]{getString(R.string.save_big_image)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //以下操作会回调这两个方法:#startPermissionSDSuccess(), #startPermissionSDFaild()
                        PermissionGen.needPermission(PhotoPagerActivity.this, REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                }).show();
    }

    /**
     * 保存图片到本地sd卡
     */
    private boolean saveImage(String filePath, Bitmap bitmap) {
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
                .getEncodedCacheKey(request);
        BinaryResource bRes = ImagePipelineFactory.getInstance()
                .getMainDiskStorageCache()
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
        photoPagerBean = null;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.image_pager_exit_animation);
    }

}
