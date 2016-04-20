package com.awen.photo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import com.awen.photo.controller.ToolBarHelper;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class BaseActivity extends AppCompatActivity {

    public Toolbar toolbar;
    private boolean openToolBar = true;//true:开启toolbar,false:不开启
    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        // 5.0系统以上才开启沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    /**
     * 1、从写此方法，如果{@link BaseActivity#openToolBar} is true,open toolBar<p>
     *这里默认开启ToolBar,如果不需要开启的在{@link #setContentView(int)}之前调用这个:{@code openToolBar = false;}，注意，如果不开启ToolBar,不要调用这里的toolBar对象
     * @param layoutResID
     */
    public void setContentView(int layoutResID) {
        if (openToolBar) {
            ToolBarHelper mToolBarHelper = new ToolBarHelper(this, layoutResID);
            toolbar = mToolBarHelper.getToolBar();
            setContentView(mToolBarHelper.getContentView());
            //把toolbar设置到Activity中
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
        } else {
            setContentView(View.inflate(this, layoutResID, null));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isOpenToolBar() {
        return openToolBar;
    }

    public void setOpenToolBar(boolean openToolBar) {
        this.openToolBar = openToolBar;
    }
}
