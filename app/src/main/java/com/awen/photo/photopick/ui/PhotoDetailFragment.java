package com.awen.photo.photopick.ui;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.awen.photo.R;
import com.awen.photo.photopick.util.AppPathUtil;
import com.awen.photo.photopick.util.ImageUtils;
import com.awen.photo.photopick.widget.photodraweeview.OnViewTapListener;
import com.awen.photo.photopick.widget.photodraweeview.PhotoDraweeView;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.animated.base.AnimatedDrawable;
import com.facebook.imagepipeline.animated.factory.AnimatedDrawableFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableAnimatedImage;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 单张图片显示Fragment
 *
 * @author Homk-M <Awentljs@gmail.com>
 */
public class PhotoDetailFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private String mImageUrl = "";
    private PhotoDraweeView mPhotoDraweeView;
    private Map<String,Bitmap> cacheBitmap = new HashMap<>();
    private boolean saveImage;
    private String saveImageLocalPath;

    public static PhotoDetailFragment newInstance(String imageUrl,boolean saveImage,String saveImageLocalPath) {
        final PhotoDetailFragment f = new PhotoDetailFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        args.putBoolean("saveImage", saveImage);
        args.putString("saveImageLocalPath", saveImageLocalPath);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            mImageUrl = bundle.getString("url");
            saveImage = bundle.getBoolean("saveImage");
            saveImageLocalPath = bundle.getString("saveImageLocalPath");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mPhotoDraweeView = (PhotoDraweeView) v.findViewById(R.id.image);
        mPhotoDraweeView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                getActivity().finish();
            }
        });

        if(saveImage) {
            mPhotoDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    saveImageDialog();
                    return true;
                }
            });
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Uri uri;
        if(mImageUrl.contains("file://")){
            String splitUrl = mImageUrl.substring(7,mImageUrl.length());
            uri = Uri.fromFile(new File(splitUrl));
            Log.e(TAG,"file path = " + splitUrl);
        }else if(mImageUrl.contains("#")){
            uri = Uri.fromFile(new File(mImageUrl));
            Log.e(TAG,"file path = " + mImageUrl);
        }else{
            uri = Uri.parse(mImageUrl);
        }
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        if (imageInfo == null || mPhotoDraweeView == null) {
                            Log.e(TAG,"imageInfo = null");
                            return;
                        }
                        mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                    }
                })
                .setOldController(mPhotoDraweeView.getController())
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(request,null);

        dataSource.subscribe(new BaseBitmapDataSubscriber() {

                                 @Override
                                 public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                     // You can use the bitmap in only limited ways
                                     // No need to do any cleanup.
                                     if(bitmap != null){
                                         Log.e(TAG,"bitmap != null,mImageUrl = " +mImageUrl);
                                         cacheBitmap.put(mImageUrl,bitmap);
                                     }else{
                                         Log.e(TAG,"bitmap == null,mImageUrl = " +mImageUrl);
                                     }
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                     // No cleanup required here.
                                 }
                             },
                CallerThreadExecutor.getInstance());
        mPhotoDraweeView.setController(controller);
    }

    private void saveImageDialog(){
        new AlertDialog.Builder(getActivity())
                .setItems(new String[]{getString(R.string.save_big_image)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cacheBitmap == null || cacheBitmap.isEmpty()) {
                            Toast.makeText(getActivity(),R.string.saved_faild,Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.e(TAG, "sd card image path = " + (saveImageLocalPath == null ? AppPathUtil.getBigBitmapCachePath() : saveImageLocalPath));
                        //保存图片到本地
                        Bitmap bitmap = cacheBitmap.get(mImageUrl);
                        String fileName = mImageUrl.substring(mImageUrl.lastIndexOf("/") + 1, mImageUrl.length());
                        if (!fileName.contains(".jpg") && !fileName.contains(".png") && !fileName.contains(".jpeg")) {
                            fileName = fileName + ".jpg";
                        }
                        String filePath = (saveImageLocalPath == null ? AppPathUtil.getBigBitmapCachePath() : saveImageLocalPath) + fileName;
                        Log.e(TAG, "save image fileName = " + fileName);
                        Log.e(TAG, "save image path = " + filePath);
                        boolean state = ImageUtils.saveImageToGallery(filePath, bitmap);
                        String tips = state ? getString(R.string.save_image_aready, filePath) : getString(R.string.saved_faild);
                        Toast.makeText(getActivity(),tips,Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
}
