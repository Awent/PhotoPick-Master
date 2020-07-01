package com.simaple.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;

import com.awen.photo.photopick.ui.PhotoPagerActivity;
import com.simaple.MyPhotoBean;
import com.simaple.R;

import java.util.ArrayList;

/**
 * 自定义图片浏览界面
 * Created by Awen <Awentljs@gmail.com>
 */

public class MyPhotoPagerActivity extends PhotoPagerActivity {

    private TextView title;
    private EditText et;
    private ArrayList<MyPhotoBean> list;

    @Override
    protected void setCustomView(@LayoutRes int layoutId) {
        super.setCustomView(R.layout.activity_custom_pager);
    }

    @Override
    protected void init() {
        super.init();
        et = (EditText) getCustomView().findViewById(R.id.et);
        title = (TextView) getCustomView().findViewById(R.id.textView);
//        title.setText(currentPosition + "/" + photoPagerBean.getBigImgUrls().size());
        Button button = (Button) getCustomView().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyPhotoPagerActivity.this,"我是自定义的图片浏览器",Toast.LENGTH_LONG).show();
            }
        });
        setIndicatorVisibility(false);//设置Indicator不可见

        getCustomView().findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //获取MainActivity传递过来的数据
        Bundle bundle = getBundle();
        if(bundle != null) {
            list = bundle.getParcelableArrayList("test_bundle");
            if (list != null && !list.isEmpty()){
                //you can do something
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        title.setText(currentPosition + "/" + photoPagerBean.getBigImgUrls().size());
        Toast.makeText(MyPhotoPagerActivity.this,"当前位置的图片地址 = " + photoPagerBean.getBigImgUrls().get(position),Toast.LENGTH_LONG).show();
        if (list != null && !list.isEmpty()){
            et.setText(list.get(position).getContent());
        }
    }

    @Override
    protected boolean onSingleClick() {
        Toast.makeText(MyPhotoPagerActivity.this,"单击图片",Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onLongClick(View view) {
        Toast.makeText(MyPhotoPagerActivity.this,"长按图片",Toast.LENGTH_LONG).show();
        //saveImage();//保存图片到图库
        return true;
    }
}
