package com.awen.photo.photopick.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import com.awen.photo.photopick.widget.photodraweeview.OnTouchEventAndScaleChangeListener;

/**
 * 仿微信图片下拉关闭效果，为了达到背景渐变效果，下拉移动的是viewpager，viewpager的父容器作为背景渐变的作用<br>
 * 解决了多指操作和滑动冲突的问题
 * Created by Awen <Awentljs@gmail.com>
 */

public class ScalePhotoView extends FrameLayout implements OnTouchEventAndScaleChangeListener {

    public final String TAG = getClass().getSimpleName();

    private static final int TOUCH_MODE_NONE = 0;
    private static final int TOUCH_MODE_POINTER = 1;//多指操作
    private static final int TOUCH_MODE_POINTER_CHILD = 2;//PhotoDraweeView的多指操作
    private static final float MIN_SCALE_WEIGHT = 0.25f;
    private static final int DURATION = 200;
    private static final float DRAG_GAP_PX = 50.0f;

    private int touchMode = TOUCH_MODE_NONE;
    private float pointerDownX; //记录多指操作中第一个手指按下屏幕的坐标点,图片归位要以这个为准
    private float pointerDownY;
    private float downX;
    private float downY;
    private int screenHeight;

    private boolean isDragging; //是否正在移动
    private boolean isFling;    //是否正在归位
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
        if (!isOpenDownAnimate || isFling) {
            return false;
        }
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                downY = ev.getRawY();
                isDragging = false;
                touchMode = TOUCH_MODE_NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = ev.getRawX() - downX;
                float deltaY = ev.getRawY() - downY;
                if (touchMode != TOUCH_MODE_POINTER && (isDragging || deltaY > DRAG_GAP_PX)) {
                    if (onViewTouchListener != null) {
                        onViewTouchListener.onMoving(deltaX, deltaY);
                    }
                    if (downX == 0f || deltaY == 0) {//这里是防止手指多次快速按下移动导致图片错位的方案
                        downX = ev.getRawX();
                        downY = ev.getRawY();
                    }
                    isDragging = true;
                    onDrag(ev.getRawX(), ev.getRawY());
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                onTouchActivePointer(ev);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onTouchActivePointer(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                final float upX = ev.getRawX();
                final float upY = ev.getRawY();
                if (touchMode == TOUCH_MODE_NONE) {
                    if (upY > downY && Math.abs(upY - downY) > screenHeight >> 3) {
                        if (onViewTouchListener != null) {
                            finishActivity();
                        } else {
                            onFling(upX, upY);
                        }
                    } else {
                        onFling(upX, upY);
                    }
                } else if (touchMode == TOUCH_MODE_POINTER) {//这里是双指操作，不作关闭activity判断，只是图片归位
                    onFling(pointerDownX, pointerDownY);
                } else {
                    onFling(upX, upY);
                }
                isDragging = false;
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isOpenDownAnimate || isFling) {
            return false;
        }
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                downY = ev.getRawY();
                isDragging = false;
                touchMode = TOUCH_MODE_NONE;
                pointerDownX = 0f;
                pointerDownY = 0f;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = ev.getRawY() - downY;
                if (isScaleFinish && touchMode != TOUCH_MODE_POINTER_CHILD && deltaY > DRAG_GAP_PX) {//不是PhotoDraweeView的多指操作，是下拉，进行拦截
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
    }

    private void onTouchActivePointer(MotionEvent ev) {
        if (pointerDownX == 0f || pointerDownY == 0f) {
            pointerDownX = ev.getRawX();
            pointerDownY = ev.getRawY();
        }
        touchMode = TOUCH_MODE_POINTER;
    }

    /**
     * 图片归位,移动到原来位置
     */
    private void onFling(final float upX, final float upY) {
        if (upY != downY) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(upY, downY);
            valueAnimator.setDuration(DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float Y = (float) animation.getAnimatedValue();
                    float percent = (Y - downY) / (upY - downY);
                    float X = percent * (upX - downX) + downX;
                    onDrag(X, Y);
                    if (Y == downY) {
                        downY = 0;
                        downX = 0;
                    }
                }
            });
            valueAnimator.addListener(flingAnimatorListenerAdapter);
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
                    onDrag(X, Y);
                    if (X == downX) {
                        downY = 0;
                        downX = 0;
                    }
                }

            });
            valueAnimator.addListener(flingAnimatorListenerAdapter);
            valueAnimator.start();
        }
    }

    private AnimatorListenerAdapter flingAnimatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationCancel(Animator animation) {
            isFling = false;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            isFling = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isFling = false;
        }
    };

    private void onDrag(float dx, float dy) {
        float deltaX = dx - downX;
        float deltaY = dy - downY;
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
        finishDeltaY = deltaY;
    }

    private float finishDeltaY;

    private void finishActivity() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(finishDeltaY, screenHeight);
        valueAnimator.setDuration(350L);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float Y = (float) animation.getAnimatedValue();
                viewPager.setTranslationY(Y);
                float scale = Math.min(Math.max(1 - Math.abs(Y) / screenHeight, 0), 1);
                float alpha = scale * 255;
                setBackgroundColor(Color.argb((int) alpha, 0, 0, 0));
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setBackgroundColor(Color.argb(0, 0, 0, 0));
                if (onViewTouchListener != null) {
                    onViewTouchListener.onFinish();
                }
            }
        });
        if (onViewTouchListener != null) {
            onViewTouchListener.onFinishStart();
        }
        valueAnimator.start();
    }

    public void setOpenDownAnimate(boolean openDownAnimate) {
        isOpenDownAnimate = openDownAnimate;
    }

    private boolean isScaleFinish = true;

    public void setScaleFinish(boolean scaleFinish) {
        isScaleFinish = scaleFinish;
    }

    public OnTouchEventAndScaleChangeListener getOnTouchEventAndScaleChangeListener() {
        return this;
    }

    @Override
    public void onPhotoTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
            touchMode = TOUCH_MODE_POINTER_CHILD;
        }
    }

    @Override
    public void onPhotoScaleChange(float matrixScale) {
        isScaleFinish = matrixScale >= 0.99 && matrixScale < 1.1;//如果是true,表明PhotoDraweeView已经缩放回原图
    }

    @Override
    public void onPhotoScaleEnd(float matrixScale) {
        touchMode = TOUCH_MODE_POINTER_CHILD;
        isScaleFinish = matrixScale >= 0.99 && matrixScale < 1.1;//如果是true,表明PhotoDraweeView已经缩放回原图
    }

    public interface onViewTouchListener {

        /**
         * 滑动结束,销毁
         */
        void onFinish();

        /**
         * 滑动结束,销毁前
         */
        void onFinishStart();

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
