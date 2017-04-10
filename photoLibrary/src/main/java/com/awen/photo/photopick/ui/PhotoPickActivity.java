package com.awen.photo.photopick.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import com.awen.photo.photopick.bean.PhotoPickBean;
import com.awen.photo.photopick.controller.PhotoPickConfig;
import com.awen.photo.photopick.controller.PhotoPreviewConfig;
import com.awen.photo.photopick.loader.MediaStoreHelper;
import com.awen.photo.photopick.util.PermissionUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * 图片选择器<br>
 * 还可以扩展更多，暂时没时间扩展了<br>
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
    public static final int REQUEST_CODE_CLIPIC = 1;//裁剪头像
    public static final int REQUEST_CODE_PERMISSION_SD = 200;//获取sd卡读写权限
    public static final int REQUEST_CODE_PERMISSION_CAMERA = 100;//获取拍照权限

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private PhotoGalleryAdapter galleryAdapter;
    private PhotoPickAdapter adapter;
    private PhotoPickBean pickBean;
    private Uri cameraUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pick);
        //you can set some configs in bundle
        Bundle bundle = getIntent().getBundleExtra(PhotoPickConfig.EXTRA_PICK_BUNDLE);
        if (bundle == null) {
            throw new NullPointerException("bundle is null,please init it");
        }
        pickBean = bundle.getParcelable(PhotoPickConfig.EXTRA_PICK_BEAN);
        if (pickBean == null) {
            finish();
            return;
        }
        //以下操作会回调这两个方法:#startPermissionSDSuccess(), #startPermissionSDFaild()
        PermissionGen.needPermission(this, REQUEST_CODE_PERMISSION_SD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void startInit() {
        toolbar.setTitle(R.string.select_photo);
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, pickBean.getSpanCount()));
        adapter = new PhotoPickAdapter(this, pickBean);
        recyclerView.setAdapter(adapter);

        RecyclerView gallery_rv = (RecyclerView) this.findViewById(R.id.gallery_rv);
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
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        final long start = System.currentTimeMillis();
//        MediaStoreHelper.getPhotoDirs(this, bundle, new MediaStoreHelper.PhotosResultCallback() {
//            @Override
//            public void onResultCallback(List<PhotoDirectory> directories) {
//                DLog.e(TAG,"use time = " + (System.currentTimeMillis() - start));
//                dismissProgressDialog();
//                adapter.refresh(directories.get(0).getPhotos());
//                galleryAdapter.refresh(directories);
//            }
//        });
        MediaStoreHelper.getPhotoDirs(this, new MediaStoreHelper.PhotosResultCallback() {
            @Override
            public void onResultCallback(final List<PhotoDirectory> directories) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "use time = " + (System.currentTimeMillis() - start));
                        progressDialog.dismiss();
                        adapter.refresh(directories.get(0).getPhotos());
                        galleryAdapter.refresh(directories);
                    }
                });

            }
        });

        slidingUpPanelLayout = (SlidingUpPanelLayout) this.findViewById(R.id.slidingUpPanelLayout);
        slidingUpPanelLayout.setAnchorPoint(0.5f);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e(TAG, "notifyPermissionsChange");
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = REQUEST_CODE_PERMISSION_CAMERA)
    private void selectPicFromCameraSuccess() {
        Log.e(TAG, "selectPicFromCameraSuccess");
        selectPicFromCamera();
    }

    @PermissionFail(requestCode = REQUEST_CODE_PERMISSION_CAMERA)
    private void selectPicFromCameraFailed() {
        Log.e(TAG, "selectPicFromCameraFailed");
        PermissionUtil.showSystemSettingDialog(this, getString(R.string.permission_tip_camera));
    }

    @PermissionSuccess(requestCode = REQUEST_CODE_PERMISSION_SD)
    private void startPermissionSDSuccess() {
        startInit();
        Log.e(TAG, "startPermissionSuccess");
    }

    @PermissionFail(requestCode = REQUEST_CODE_PERMISSION_SD)
    private void startPermissionSDFailed() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.permission_tip_SD))
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PermissionUtil.startSystemSettingActivity(PhotoPickActivity.this);
                finish();
            }
        }).setCancelable(false).show();
        Log.e(TAG, "startPermissionFailed");
    }

    /**
     * 启动Camera拍照
     */
    public void selectPicFromCamera() {
        if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, R.string.cannot_take_pic, Toast.LENGTH_SHORT).show();
            return;
        }
        // 直接将拍到的照片存到手机默认的文件夹
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        cameraUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!pickBean.isClipPhoto()) {
            getMenuInflater().inflate(R.menu.menu_ok, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ok) {
            if (adapter != null && !adapter.getSelectPhotos().isEmpty()) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(PhotoPickConfig.EXTRA_STRING_ARRAYLIST, adapter.getSelectPhotos());
                setResult(Activity.RESULT_OK, intent);
//                String s = "已选择的图片大小 = " + adapter.getSelectPhotos().size() + "\n" + adapter.getSelectPhotos().toString();
//                Toast.makeText(this, s, Toast.LENGTH_LONG).show();
                finish();
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
        switch (requestCode) {
            case REQUEST_CODE_CAMERA://相机
                findPhoto();
                break;
            case REQUEST_CODE_CLIPIC://头像裁剪
                if (data != null) {
                    String photPath = data.getStringExtra(ClipPictureActivity.CLIPED_PHOTO_PATH);
                    if (photPath != null) {
                        ArrayList<String> pic = new ArrayList<>();
                        pic.add(photPath);
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra(PhotoPickConfig.EXTRA_STRING_ARRAYLIST, pic);
                        setResult(Activity.RESULT_OK, intent);
//                        Toast.makeText(this, "已裁剪的图片地址 = \n" + photPath, Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(this, R.string.unable_find_pic, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, R.string.unable_find_pic, Toast.LENGTH_LONG).show();
                }
                break;
            case PhotoPreviewConfig.REQUEST_CODE:
                boolean isBackPressed = data.getBooleanExtra("isBackPressed", false);
                if (!isBackPressed) {//如果上个activity不是按了返回键的，就是按了"发送"按钮
                    setResult(Activity.RESULT_OK, data);
                    finish();
                } else {//用户按了返回键，合并用户选择的图片集合
                    ArrayList<String> photoLists = data.getStringArrayListExtra(PhotoPickConfig.EXTRA_STRING_ARRAYLIST);
                    if (photoLists == null) {
                        return;
                    }
                    ArrayList<String> selectedList = adapter.getSelectPhotos();//之前已经选了的图片
                    List<String> deleteList = new ArrayList<>();//这是去图片预览界面需要删除的图片
                    for (String s : selectedList) {
                        if (!photoLists.contains(s)) {
                            deleteList.add(s);
                        }
                    }
                    selectedList.removeAll(deleteList);//删除预览界面取消选择的图片
                    deleteList.clear();
                    //合并相同的数据
                    HashSet<String> set = new HashSet<>(photoLists);
                    for (String s : selectedList) {
                        set.add(s);
                    }
                    selectedList.clear();
                    selectedList.addAll(set);
                    toolbar.setTitle(adapter.getTitle());
                    adapter.notifyDataSetChanged();

//                    for(String s: set){
//                        Log.e(TAG,s);
//                    }
                }
                break;
        }
    }

    private void findPhoto() {
        String picturePath = null;
        Uri uri = cameraUri;
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
                    Toast.makeText(this, R.string.unable_find_pic, Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, R.string.unable_find_pic, Toast.LENGTH_LONG).show();
        } else {
            if (pickBean.isClipPhoto()) {//拍完照之后，如果要启动头像裁剪，则去裁剪再吧地址传回来
                adapter.startClipPic(picturePath);
            } else {
                ArrayList<String> pic = new ArrayList<>();
                pic.add(picturePath);
                Intent intent = new Intent();
                intent.putStringArrayListExtra(PhotoPickConfig.EXTRA_STRING_ARRAYLIST, pic);
                setResult(Activity.RESULT_OK, intent);
//                Toast.makeText(this, "已返回的拍照图片地址 = \n" + picturePath, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.image_pager_exit_animation);
    }

}
