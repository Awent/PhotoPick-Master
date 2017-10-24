package com.awen.photo.photopick.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.awen.photo.photopick.util.ViewUtil;

/**
 * 仿微信图片下拉关闭效果，为了达到背景渐变效果，下拉移动的是viewpager，viewpager的父容器作为背景渐变的作用
 * Created by Awen <Awentljs@gmail.com>
 */

public class ScalePhotoView extends FrameLayout {

    public final String TAG = getClass().getSimpleName();

    public static final float MIN_SCALE_WEIGHT = 0.25f;
    public static final int DURATION = 300;
    public static final float DRAG_GAP_PX = 50.0f;

    private float downX;
    private float downY;
    private int screenHeight;

    private boolean isMoving;
    private View viewPager;
    /**
     * 是否开启下滑关闭activity，默认开启。类似微信的图片浏览，可下滑关闭一样，但是没有图片归位效果
     */
    private boolean isOpenDownAnimate = true;

    public ScalePhotoView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ScalePhotoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScalePhotoView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        screenHeight = ViewUtil.getDisplayHeight(context);
        setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewPager = getChildAt(0);//获取可以手动移动的view
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isOpenDownAnimate) {
            return false;
        }
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                downY = ev.getRawY();
                isMoving = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = ev.getRawX() - downX;
                float deltaY = ev.getRawY() - downY;
                if (isMoving || deltaY > DRAG_GAP_PX) {
                    if (onViewTouchListener != null) {
                        onViewTouchListener.onMoving(deltaX, deltaY);
                    }
                    toMoving(ev.getRawX(), ev.getRawY());
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                final float upX = ev.getRawX();
                final float upY = ev.getRawY();
//                Log.e(TAG, "downY = " + downY);
//                Log.e(TAG, "upY = " + upY);
                if (upY > downY &&  Math.abs(upY - downY) > screenHeight >> 3) {
                    if (onViewTouchListener != null) {
                        onViewTouchListener.onFinish();
                    } else {
                        reSet(upX, upY);
                    }
                } else {
                    reSet(upX, upY);
                }
                isMoving = false;
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isOpenDownAnimate) {
            return false;
        }
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                downY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = ev.getRawY() - downY;
                if(deltaY > DRAG_GAP_PX){//是下拉，进行拦截
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
    }

    /**
     * 图片归位,移动到原来位置
     */
    private void reSet(final float upX, final float upY) {
        if (upY != downY) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(upY, downY);
            valueAnimator.setDuration(DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float Y = (float) animation.getAnimatedValue();
                    float percent = (Y - downY) / (upY - downY);
                    float X = percent * (upX - downX) + downX;
                    toMoving(X, Y);
                    if (Y == downY) {
                        downY = 0;
                        downX = 0;
                    }
                }
            });
            valueAnimator.start();
        } else if (upX != downX) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(upX, downX);
            valueAnimator.setDuration(DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float X = (float) animation.getAnimatedValue();
                    float percent = (X - downX) / (upX - downX);
                    float Y = percent * (upY - downY) + downY;
                    toMoving(X, Y);
                    if (X == downX) {
                        downY = 0;
                        downX = 0;
                    }
                }
            });
            valueAnimator.start();
        }
    }

    private void toMoving(float movingX, float movingY) {
        isMoving = true;
        float deltaX = movingX - downX;
        float deltaY = movingY - downY;
        float scale = 1f;
        if (deltaY > 0) {
            scale = 1 - Math.abs(deltaY) / screenHeight;
        }
        //移动
        viewPager.setTranslationX(deltaX);
        viewPager.setTranslationY(deltaY);
        //缩放
        scale = Math.min(Math.max(scale, MIN_SCALE_WEIGHT), 1);
        viewPager.setScaleX(scale);
        viewPager.setScaleY(scale);
        //设置背景颜色
        float alpha = scale * 255;
        setBackgroundColor(Color.argb((int) alpha, 0, 0, 0));
    }

    public void setOpenDownAnimate(boolean openDownAnimate) {
        isOpenDownAnimate = openDownAnimate;
    }

    public interface onViewTouchListener {

        /**
         * 滑动结束,销毁
         */
        void onFinish();

        /**
         * 正在滑动
         */
        void onMoving(float deltaX, float deltaY);

    }

    private onViewTouchListener onViewTouchListener;

    public void setOnViewTouchListener(ScalePhotoView.onViewTouchListener onViewTouchListener) {
        this.onViewTouchListener = onViewTouchListener;
    }
}
