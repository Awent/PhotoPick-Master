package com.awen.photo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.awen.photo.photopick.bean.PhotoPagerBean;
import com.awen.photo.photopick.bean.PhotoPickBean;
import com.awen.photo.photopick.util.PhotoPagerConfig;
import com.awen.photo.photopick.util.PhotoPickConfig;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar.setNavigationIcon(null);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                //方法1
                new PhotoPickConfig.Builder(this)
                        .pickMode(PhotoPickConfig.MODE_MULTIP_PICK)
                        .maxPickSize(15)
                        .showCamera(false)
                        .build();

                //方法2
//                PhotoPickBean bean = new PhotoPickBean();
//                bean.setMaxPickSize(15);
//                bean.setShowCamera(false);
//                bean.setSpanCount(3);
//                bean.setPickMode(PhotoPickConfig.MODE_MULTIP_PICK);
//                new PhotoPickConfig.Builder(this)
//                        .setPhotoPickBean(bean)
//                        .build();
                break;
            case R.id.button4:
                new PhotoPickConfig.Builder(this)
                        .pickMode(PhotoPickConfig.MODE_MULTIP_PICK)
                        .maxPickSize(15)
                        .showCamera(true)
                        .build();
                break;
            case R.id.button2:
                new PhotoPickConfig.Builder(this)
                        .pickMode(PhotoPickConfig.MODE_SINGLE_PICK)
                        .clipPhoto(true)
                        .build();
                break;
            case R.id.button3:
                new PhotoPagerConfig.Builder(this)
                        .setBigImageUrls(ImageProvider.getImageUrls())
                        .setSavaImage(true)
//                        .setPosition(2)
//                        .setSaveImageLocalPath("这里是你想保存的图片地址")
                        .build();
                break;
            case R.id.button5:
                //方法1
                new PhotoPagerConfig.Builder(this)
                        .setBigImageUrls(ImageProvider.getBigImgUrls())
                        .setLowImageUrls(ImageProvider.getLowImgUrls())
                        .setSavaImage(true)
                        .build();

                //方法2,一张一张图片地添加
//                new PhotoPagerConfig.Builder(this)
//                        //添加大图
//                        .addSingleBigImageUrl("http://120.27.113.153/frogdrfs/2016/08/22/0ac279d511de46a0858a1812efe9a1ce.jpg")
//                        .addSingleBigImageUrl("http://120.27.113.153/frogdrfs/2016/08/29/62e2cd190a174e6c83ea3e948ecba66a.jpg")
//                        .addSingleLowImageUrl("http://120.27.113.153/frogdrfs/2016/08/22/0ac279d511de46a0858a1812efe9a1ce.jpg&64X64.jpg")
//                        .addSingleLowImageUrl("http://120.27.113.153/frogdrfs/2016/08/29/62e2cd190a174e6c83ea3e948ecba66a.jpg&64X64.jpg")
//                        .setSavaImage(true)
//                        .build();

                //方法3
                //先构建实体类,在实体类里设置好参数
//                PhotoPagerBean bean = new PhotoPagerBean();
//                bean.setBigImgUrls(ImageProvider.getBigImgUrls());
//                bean.setLowImgUrls(ImageProvider.getLowImgUrls());
//                bean.setSaveImage(true);
//                //再设置实体类
//                new PhotoPagerConfig.Builder(this)
//                        .setPhotoPagerBean(bean)
//                        .build();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        switch (requestCode) {
            case PhotoPickConfig.PICK_REQUEST_CODE://图片
                ArrayList<String> photoLists = data.getStringArrayListExtra(PhotoPickConfig.EXTRA_STRING_ARRAYLIST);
                if (photoLists != null && !photoLists.isEmpty()) {
                    Log.i("MainActivity","selected photos = " + photoLists.toString());
                    File file = new File(photoLists.get(0));
                    if (file.exists()) {
                        //you can do something

                    } else {
                        //toast error
                    }
                }
                break;
        }
    }
}
