package com.awen.photo.controller;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.awen.photo.R;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class ToolBarHelper {

    /*base view*/
    private LinearLayout mContentView;

    /*toolbar*/
    private Toolbar mToolBar;

    public ToolBarHelper(Context context, int layoutId) {
        init(context,R.layout.toolbar_layout,layoutId);
    }

    public ToolBarHelper(Context context, int toolBarId, int layoutId) {
        init(context,toolBarId,layoutId);
    }

    private void init(Context context,int toolBarId, int layoutId){
        LayoutInflater mInflater = LayoutInflater.from(context);
        /*初始化整个内容-直接创建一个帧布局，作为视图容器的父容器*/
        mContentView = new LinearLayout(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setOrientation(LinearLayout.VERTICAL);
        mContentView.setLayoutParams(params);

        /*初始化toolbar-通过inflater获取toolbar的布局文件*/
        View toolbar = mInflater.inflate(toolBarId, mContentView);
        mToolBar = (Toolbar) toolbar.findViewById(R.id.toolbar);

        /**初始化用户定义的布局*/
        View mUserView = mInflater.inflate(layoutId, null);
        mContentView.addView(mUserView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public LinearLayout getContentView() {
        return mContentView;
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }
}
