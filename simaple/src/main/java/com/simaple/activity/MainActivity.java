package com.simaple.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.awen.photo.FrescoImageLoader;
import com.awen.photo.photopick.bean.PhotoResultBean;
import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.awen.photo.photopick.controller.PhotoPickConfig;
import com.awen.photo.photopick.controller.PhotoPreviewConfig;
import com.awen.photo.photopick.ui.PhotoPagerActivity;
import com.simaple.ImageProvider;
import com.simaple.MyPhotoBean;
import com.simaple.R;
import com.simaple.UserBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片归版权者所有
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
        findViewById(R.id.button10).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button://图库
                //方法1
                new PhotoPickConfig.Builder(this)
                        .pickModeMulti()
                        .maxPickSize(15)
                        .showCamera(false)
//                        .showGif(false)
                        .build();

                break;
            case R.id.button4://图库(可以启动拍照)
                new PhotoPickConfig.Builder(this)
                        .pickModeMulti()
                        .maxPickSize(15)
                        .showCamera(true)
                        .setOriginalPicture(true)//让用户可以选择原图
                        .setOnPhotoResultCallback(new PhotoPickConfig.Builder.OnPhotoResultCallback() {
                            @Override
                            public void onResult(PhotoResultBean result) {
                                Log.e("MainActivity", "result = " + result.getPhotoLists().size());
                            }
                        })
                        .build();
                break;
            case R.id.button2://裁剪头像
                new PhotoPickConfig.Builder(this)
                        .clipPhoto(true)
                        .build();
                break;
            case R.id.button3://查看(网络)大图
                new PhotoPagerConfig.Builder<String>(this)
                        .setBigImageUrls(ImageProvider.getImageUrls())
                        .setSavaImage(true)
//                        .setPosition(2)
//                        .setSaveImageLocalPath("这里是你想保存的图片地址")
                        .setOnPhotoSaveCallback(new PhotoPagerConfig.Builder.OnPhotoSaveCallback() {//保存网络图片到本地图库的回调
                            @Override
                            public void onSaveImageResult(String localFilePath) {
                                if (localFilePath != null) {
                                    Toast.makeText(MainActivity.this, "图片保存成功，本地地址：" + localFilePath, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "图片保存失败", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .build();
                break;
            case R.id.button5://大图展示前先显示小图
                //方法1
                new PhotoPagerConfig.Builder<String>(this)
                        .setBigImageUrls(ImageProvider.getBigImgUrls())
                        .setSmallImageUrls(ImageProvider.getSmallImgUrls())
                        .setSavaImage(true)
                        .build();

                //方法2,一张一张图片地添加
//                new PhotoPagerConfig.Builder(this)
//                        //添加大图
//                        .addSingleBigImageUrl("http://120.27.113.153/frogdrfs/2016/08/22/0ac279d511de46a0858a1812efe9a1ce.jpg")
//                        .addSingleBigImageUrl("http://120.27.113.153/frogdrfs/2016/08/29/62e2cd190a174e6c83ea3e948ecba66a.jpg")
//                        .addSingleSmallImageUrl("http://120.27.113.153/frogdrfs/2016/08/22/0ac279d511de46a0858a1812efe9a1ce.jpg&64X64.jpg")
//                        .addSingleSmallImageUrl("http://120.27.113.153/frogdrfs/2016/08/29/62e2cd190a174e6c83ea3e948ecba66a.jpg&64X64.jpg")
//                        .setSavaImage(true)
//                        .build();

                //方法3
                //先构建实体类,在实体类里设置好参数
//                PhotoPagerBean bean = new PhotoPagerBean();
//                bean.setBigImgUrls(ImageProvider.getBigImgUrls());
//                bean.setSmallImgUrls(ImageProvider.getSmallImgUrls());
//                bean.setSaveImage(true);
//                //再设置实体类
//                new PhotoPagerConfig.Builder(this)
//                        .setPhotoPagerBean(bean)
//                        .build();
                break;
            case R.id.button6://获取缓存大小:
                ((Button) findViewById(R.id.button6)).setText("缓存大小：" + FrescoImageLoader.getSDCacheSizeFormat());
                break;
            case R.id.button7://清除所有缓存
                FrescoImageLoader.clearCaches();
                ((Button) findViewById(R.id.button6)).setText("缓存大小：" + FrescoImageLoader.getSDCacheSizeFormat());
                break;
            case R.id.button8://跳到自定义view界面
                /**
                 * 1、传递数据：实现Parcelable接口，把你想传递的数据封装进Bundle，然后.setBundle(bundle)即可
                 * 2、获取数据：Bundle bundle = getBundle();
                 if(bundle != null) {
                 list = bundle.getParcelableArrayList("test_bundle");
                 if (list != null && !list.isEmpty()){
                 //you can do something
                 }
                 }
                 */
                ArrayList<MyPhotoBean> list = new ArrayList<>();
                for (int i = 0; i < ImageProvider.getImageUrls().size(); i++) {
                    MyPhotoBean bean = new MyPhotoBean();
                    bean.setId(i);
                    bean.setContent("content = 你是否还记得？那年我们在春暖花开里相遇，我们都用真情，守护着相遇后的每一秒光阴。每一次与你目光碰触，你的眼睛清澈如水，深邃如诗，绽不尽的芳华，浪漫依依。" + "---" + i);
                    list.add(bean);
                }
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("test_bundle", list);

                new PhotoPagerConfig.Builder<String>(this, MyPhotoPagerActivity.class)
                        .setBigImageUrls(ImageProvider.getImageUrls())
                        .setSavaImage(true)
                        .setBundle(bundle) //传递自己的数据，如果数据中包含java bean，必须实现Parcelable接口
                        .setOpenDownAnimate(false)
                        .build();
                break;
            case R.id.button9://跳到自定义的PhotoPagerActivity(kotlin写法)
                Bundle mBundle = new Bundle();
                mBundle.putLong("user_id",100000L);
                new PhotoPagerConfig.Builder<String>(this, CustomPhotoPageActivity.class)
                        .setBigImageUrls(ImageProvider.getImageUrls())
                        .setSavaImage(true)
                        .setBundle(mBundle)
                        .build();
                break;
            case R.id.button10://实际开发常用写法（查看网络大图）
                //fromList的使用
                List<UserBean.User> userList = initUserData().getList();
                new PhotoPagerConfig.Builder<UserBean.User>(this, PhotoPagerActivity.class /**或者这里传入你自定义的CustomPhotoPageActivity*/)
                        .fromList(userList, new PhotoPagerConfig.Builder.OnItemCallBack<UserBean.User>() {
                            @Override
                            public void nextItem(UserBean.User item, PhotoPagerConfig.Builder<UserBean.User> builder) {
                                //一定要在这里获取你的图片字段，然后设置进去即可
                                builder.addSingleBigImageUrl(item.getAvatar());
                                builder.addSingleSmallImageUrl(item.getSmallAvatar());
                            }
                        })
                        .setSavaImage(true)
                        .build();

                //或者是以下的fromMap使用
//                Map<Integer, UserBean.User> map = initUserDataMap();
//                new PhotoPagerConfig.Builder<UserBean.User>(this, PhotoPageActivity.class)
//                        .fromMap(map, new PhotoPagerConfig.Builder.OnItemCallBack<UserBean.User>() {
//                            @Override
//                            public void nextItem(UserBean.User item, PhotoPagerConfig.Builder<UserBean.User> builder) {
//                                //在这里获取你的图片字段，然后设置进去即可
//                                builder.addSingleBigImageUrl(item.getAvatar());
//                            }
//                        })
//                        .setSavaImage(true)
//                        .build();
                break;
        }
    }

    private UserBean initUserData() {
        UserBean bean = new UserBean();
        bean.setCode(200);
        bean.setMsg("success");
        List<UserBean.User> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            UserBean.User user = new UserBean.User();
            user.setAvatar("https://wx1.sinaimg.cn/mw690/7325792bly1fx9oma87k1j21900u04jf.jpg");
            user.setSmallAvatar("https://wx1.sinaimg.cn/mw690/7325792bly1fx9oma87k1j21900u04jf.jpg");
            user.setName("name = " + i);
            user.setAge(i);
            list.add(user);
        }
        bean.setList(list);
        return bean;
    }

    private Map<Integer, UserBean.User> initUserDataMap() {
        Map<Integer, UserBean.User> map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            UserBean.User user = new UserBean.User();
            user.setAvatar("https://wx1.sinaimg.cn/mw690/7325792bly1fx9oma87k1j21900u04jf.jpg");
            user.setSmallAvatar("https://wx1.sinaimg.cn/mw690/7325792bly1fx9oma87k1j21900u04jf.jpg");
            user.setName("name = " + i);
            user.setAge(i);
            map.put(i, user);
        }
        return map;
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
                //用户选择的是否是原图
                boolean isOriginalPicture = data.getBooleanExtra(PhotoPreviewConfig.EXTRA_ORIGINAL_PIC, false);
                if (photoLists != null && !photoLists.isEmpty()) {
                    Log.i("MainActivity", "selected photos = " + photoLists.toString());
                    Toast.makeText(this, "selected photos size = " + photoLists.size() + "\n" + photoLists.toString(), Toast.LENGTH_LONG).show();
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
