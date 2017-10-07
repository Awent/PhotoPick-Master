package com.simaple;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 解决activity设置了状态栏透明，又设置了android:windowSoftInputMode="adjustResize"的时候输入框被输入法挡住的情况<br>
 *     这个作为layout的根布局(root),并设置<code>android:fitsSystemWindows="true"</code>
 * Created by Awen <Awentljs@gmail.com>
 */
public class CustomInsetsRelativeLayout extends RelativeLayout {
    private int[] mInsets = new int[4];

    public CustomInsetsRelativeLayout(Context context) {
        super(context);
    }

    public CustomInsetsRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomInsetsRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public final int[] getInsets() {
        return mInsets;
    }

    @Override
    protected final boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Intentionally do not modify the bottom inset. For some reason,
            // if the bottom inset is modified, window resizing stops working.
            // TODO: Figure out why.

            mInsets[0] = insets.left;
            mInsets[1] = insets.top;
            mInsets[2] = insets.right;

            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }

        return super.fitSystemWindows(insets);
    }
}