package com.awen.photo.photopick.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.awen.photo.BaseActivity;
import com.awen.photo.R;
import com.awen.photo.photopick.adapter.PhotoGalleryAdapter;
import com.awen.photo.photopick.adapter.PhotoPickAdapter;
import com.awen.photo.photopick.bean.Photo;
import com.awen.photo.photopick.bean.PhotoDirectory;
import com.awen.photo.photopick.loader.MediaStoreHelper;
import com.awen.photo.photopick.util.PhotoPickConfig;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择器<br>
 *     还可以扩展更多，暂时没时间扩展了<br>
 * 使用方法：<br><code>
 * new PickConfig.Builder(this)<br>
 * .pickMode(PickConfig.MODE_MULTIP_PICK)<br>
 * .maxPickSize(30)<br>
 * .spanCount(3)<br>
 * .showCamera(false) //default true<br>
 * .clipPhoto(true)   //default false<br>
 * .build();<br>
 * </code>
 * Created by Awen <Awentljs@gmail.com>
 */
public class PhotoPickActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    public static final int REQUEST_CODE_CAMERA = 0;// 拍照
    private static final int REQUEST_CODE_CLIPIC = 1;//裁剪头像

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private RecyclerView recyclerView, gallery_rv;
    private PhotoGalleryAdapter galleryAdapter;
    private PhotoPickAdapter adapter;
    private Bundle bundle;
    private int maxPickSize, pickMode, spanCount;
    private boolean showCamera, clipPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pick);
        //you can set some configs in bundle
        bundle = getIntent().getBundleExtra(PhotoPickConfig.EXTRA_PICK_BUNDLE);
        if (bundle == null) {
            throw new NullPointerException("bundle is null,please init it");
        }
        spanCount = bundle.getInt(PhotoPickConfig.EXTRA_SPAN_COUNT, PhotoPickConfig.DEFAULT_SPANCOUNT);
        pickMode = bundle.getInt(PhotoPickConfig.EXTRA_PICK_MODE, PhotoPickConfig.MODE_SINGLE_PICK);
        maxPickSize = bundle.getInt(PhotoPickConfig.EXTRA_MAX_SIZE, PhotoPickConfig.DEFAULT_PICKSIZE);
        showCamera = bundle.getBoolean(PhotoPickConfig.EXTAR_SHOW_CAMERA, PhotoPickConfig.DEFAULT_SHOW_CAMERA);
        clipPhoto = bundle.getBoolean(PhotoPickConfig.EXTAR_START_CLIP, PhotoPickConfig.DEFAULT_START_CLIP);

        toolbar.setTitle(R.string.select_photo);
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        adapter = new PhotoPickAdapter(this, spanCount, maxPickSize, pickMode, showCamera);
        recyclerView.setAdapter(adapter);

        gallery_rv = (RecyclerView) this.findViewById(R.id.gallery_rv);
        gallery_rv.setLayoutManager(new LinearLayoutManager(this));
        galleryAdapter = new PhotoGalleryAdapter(this);
        gallery_rv.setAdapter(galleryAdapter);

        adapter.setOnUpdateListener(new PhotoPickAdapter.OnUpdateListener() {
            @Override
            public void updataToolBarTitle(String title) {
                toolbar.setTitle(title);
            }
        });

        galleryAdapter.setOnItemClickListener(new PhotoGalleryAdapter.OnItemClickListener() {
            @Override
            public void onClick(List<Photo> photos) {
                if (adapter != null) {
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    adapter.refresh(photos);
                }
            }
        });
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.clip));
        dialog.setCancelable(false);
        dialog.show();
        final long start = System.currentTimeMillis();
        MediaStoreHelper.getPhotoDirs(this, bundle, new MediaStoreHelper.PhotosResultCallback() {
            @Override
            public void onResultCallback(List<PhotoDirectory> directories) {
                Log.e(TAG,"use time = " + (System.currentTimeMillis() - start));
                dialog.dismiss();
                adapter.refresh(directories.get(0).getPhotos());
                galleryAdapter.refresh(directories);
            }
        });
//        MediaStoreHelper.getPhotoDirs(this, new MediaStoreHelper.PhotosResultCallback() {
//            @Override
//            public void onResultCallback(final List<PhotoDirectory> directories) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                            Log.e(TAG,"use time = " + (System.currentTimeMillis() - start));
//                        dialog.dismiss();
//                        adapter.refresh(directories.get(0).getPhotos());
//                        galleryAdapter.refresh(directories);
//                    }
//                });
//
//            }
//        });

        slidingUpPanelLayout = (SlidingUpPanelLayout) this.findViewById(R.id.slidingUpPanelLayout);
        slidingUpPanelLayout.setAnchorPoint(0.5f);

//                    if (slidingUpPanelLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
//                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
//                    } else {
//                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//                    }
//                    if (slidingUpPanelLayout.getAnchorPoint() == 1.0f) {
//                        slidingUpPanelLayout.setAnchorPoint(0.5f);
//                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
//                    } else {
//                        slidingUpPanelLayout.setAnchorPoint(1.0f);
//                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//                    }
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });
        slidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ok) {
            if (adapter != null && !adapter.getSelectPhotos().isEmpty()) {
                if (clipPhoto) {//如果开启了图片裁剪，跳到裁剪界面
                    startClipPic(adapter.getSelectPhotos().get(0));
                } else {
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(PhotoPickConfig.EXTRA_STRING_ARRAYLIST, adapter.getSelectPhotos());
                    setResult(Activity.RESULT_OK, intent);
                    String s = "已选择的图片大小 = " + adapter.getSelectPhotos().size() + "\n" + adapter.getSelectPhotos().toString();
                    Toast.makeText(this,s,Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout != null &&
                (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                        slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CAMERA) {//相机
            findPhoto();
        } else if (requestCode == REQUEST_CODE_CLIPIC) {//头像裁剪
            if (data != null) {
                String photPath = data.getStringExtra(ClipPictureActivity.CLIPED_PHOTO_PATH);
                if (photPath != null) {
                    ArrayList<String> pic = new ArrayList<>();
                    pic.add(photPath);
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(PhotoPickConfig.EXTRA_STRING_ARRAYLIST, pic);
                    setResult(Activity.RESULT_OK, intent);
                    Toast.makeText(this,"已裁剪的图片地址 = \n" + photPath,Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this,R.string.unable_find_pic,Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this,R.string.unable_find_pic,Toast.LENGTH_LONG).show();
            }
        }
    }

    private void findPhoto() {
        String picturePath = null;
        Uri uri = adapter.getCameraUri();
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex("_data");
                picturePath = cursor.getString(columnIndex);
            } else {
                File file = new File(uri.getPath());
                if (!file.exists()) {
                    Toast.makeText(this,R.string.unable_find_pic,Toast.LENGTH_LONG).show();
                    return;
                }
                picturePath = uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        if (picturePath == null) {
            Toast.makeText(this,R.string.unable_find_pic,Toast.LENGTH_LONG).show();
        } else {
            if (clipPhoto) {//拍完照之后，如果要启动头像裁剪，则去裁剪再吧地址传回来
                startClipPic(picturePath);
            } else {
                ArrayList<String> pic = new ArrayList<>();
                pic.add(picturePath);
                Intent intent = new Intent();
                intent.putStringArrayListExtra(PhotoPickConfig.EXTRA_STRING_ARRAYLIST, pic);
                setResult(Activity.RESULT_OK, intent);
                Toast.makeText(this,"已返回的拍照图片地址 = \n" + picturePath,Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }

    private void startClipPic(String picPath) {
        Intent intent = new Intent(this, ClipPictureActivity.class);
        intent.putExtra(ClipPictureActivity.USER_PHOTO_PATH, picPath);
        startActivityForResult(intent, REQUEST_CODE_CLIPIC);
    }
}
