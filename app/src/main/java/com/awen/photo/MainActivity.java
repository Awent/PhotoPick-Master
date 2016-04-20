package com.awen.photo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                new PhotoPickConfig.Builder(this)
                        .pickMode(PhotoPickConfig.MODE_MULTIP_PICK)
                        .maxPickSize(15)
                        .showCamera(false)
                        .build();
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
                ArrayList<String> list = new ArrayList<>();
                //也可以传本地图片，格式如下：
//                list.add("file:///storage/emulated/0/frgdr/frgdr_Video/im_101460431984904.jpg");
//                list.add("file:///storage/emulated/0/frgdr/frgdr_Video/im_101460432332951.jpg");
                list.add("http://p1.wmpic.me/article/2015/06/29/1435559754_fnZtksvI.jpg");
                list.add("http://p2.wmpic.me/article/2015/06/29/1435559758_rMcNLvQq.jpg");
                list.add("http://pic1.win4000.com/pic/6/da/50bc520323.jpg");
                list.add("http://www.wmpic.me/wp-content/uploads/2014/02/20140218150739227.jpg");
                list.add("http://www.bz55.com/uploads/allimg/140904/138-140Z4092036.jpg");
                list.add("http://image.hiapk.com/pic/2012/01/10/and_pbpic_qunjie20120110_14.jpg");
                list.add("http://img.article.pchome.net/game/00/11/14/22/pic_lib/wm/Yuurimorishita16.jpg");
                list.add("http://beiwoo.cn/uploads/allimg/120621/1106001S4-3.jpg");
                list.add("http://pic2.qnpic.com/doimg/meiyuanguan/9bda3e55/");
                list.add("http://img.weibonvren.com/meinv/qita/senxia/36.jpg");
                list.add("http://www.winddesktop.com/Wallpaper/01563/Original/01563_001_3Ik8vH1i.jpg");
                list.add("http://img.yxbao.com/news/image/201503/23/61ebe76486.jpg");
                list.add("http://img1.gtimg.com/ent/pics/hv1/142/234/1470/95646562.jpg");
                list.add("http://www.weimeitupian.com/wp-content/uploads/2015/02/20150215145914389.jpg");
                list.add("http://d.3987.com/xglsnymnipadzmbz.120906/005.jpg");
                list.add("http://www.7zhan.com/uploads/allimg/131007/8-13100F91118.jpg");
                list.add("http://pic2.qnpic.com/doimg/meiyuanguan/9bda3e55/");
                new PhotoPagerConfig.Builder(this).setImageUrls(list).setSavaImage(true).build();
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
