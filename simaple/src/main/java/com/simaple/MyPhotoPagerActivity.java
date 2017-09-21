package com.simaple;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.awen.photo.photopick.ui.PhotoPagerActivity;

/**
 * 自定义图片浏览界面
 * Created by Awen <Awentljs@gmail.com>
 */

public class MyPhotoPagerActivity extends PhotoPagerActivity {

    @Override
    protected void setCustomView(int layoutId) {
        super.setCustomView(R.layout.activity_custom_pager);
    }

    @Override
    protected void init() {
        super.init();
        TextView textView = getCustomView().findViewById(R.id.textView);
        Button button = getCustomView().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyPhotoPagerActivity.this,"我是自定义的图片浏览器",Toast.LENGTH_LONG).show();
            }
        });
        setIndicatorVisibility(false);//设置Indicator不可见
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        Toast.makeText(MyPhotoPagerActivity.this,"当前位置的图片地址 = " + photoPagerBean.getBigImgUrls().get(position),Toast.LENGTH_LONG).show();
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
