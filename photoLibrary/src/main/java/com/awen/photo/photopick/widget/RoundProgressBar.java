package com.awen.photo.photopick.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.facebook.drawee.drawable.DrawableUtils;


/**
 * 圆形进度条<br>
 * @author Homk-M <Awentljs@gmail.com>
 */
public class RoundProgressBar extends Drawable {
    private static final String TAG = "RoundProgressBar";
    private Paint mRingBackgroundPaint;
    private int mRingBackgroundColor;
    // 画圆环的画笔
    private Paint mRingPaint;
    // 圆环颜色
    private int mRingColor;
    // 半径
    private float mRadius;
    // 圆环半径
    private float mRingRadius;
    // 圆环宽度
    private float mStrokeWidth;
    // 圆心x坐标
    private int mXCenter;
    // 圆心y坐标
    private int mYCenter;
    // 总进度
    private int mTotalProgress = 10000;
    // 当前进度
    private int mProgress;

    private Paint textPaint;
    //是否显示中间的百分比进度
    private boolean textIsDisplayable;
    private int textSize;
    private int textColor;

    public RoundProgressBar() {
        initAttrs();
    }

    private void initAttrs() {
        textIsDisplayable = true;
        textSize = 24;
        textColor = 0xFFEBAC0E;
        mRadius = 60;
        mStrokeWidth = 16;
        mRingBackgroundColor = 0xFFdddad7;
        mRingColor = 0xFFEBAC0E;
        mRingRadius = mRadius + mStrokeWidth / 2;
        initVariable();
    }

    private void initVariable() {
        mRingBackgroundPaint = new Paint();
        mRingBackgroundPaint.setAntiAlias(true);
        mRingBackgroundPaint.setColor(mRingBackgroundColor);
        mRingBackgroundPaint.setStyle(Paint.Style.STROKE);
        mRingBackgroundPaint.setStrokeWidth(mStrokeWidth);

        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);

        textPaint = new Paint();
        textPaint.setStrokeWidth(0);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD); // 设置字体
    }

    @Override
    public void draw(Canvas canvas) {
        drawBar(canvas, mTotalProgress, mRingBackgroundPaint);
        drawBar(canvas, mProgress, mRingPaint);
        drawText(canvas, mProgress, textPaint);
    }

    private void drawBar(Canvas canvas, int level, Paint paint) {
        if (level > 0) {
            Rect bound = getBounds();
            mXCenter = bound.centerX();
            mYCenter = bound.centerY();
            RectF oval = new RectF();
            oval.left = (mXCenter - mRingRadius);
            oval.top = (mYCenter - mRingRadius);
            oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
            oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
            canvas.drawArc(oval, -90, ((float) level / mTotalProgress) * 360, false, paint); //
        }
    }

    /**
     * 画进度百分比
     */
    private void drawText(Canvas canvas, int level, Paint paint) {
        if (level > 0 && textIsDisplayable) {
            Rect bound = getBounds();
            mXCenter = bound.centerX();
            mYCenter = bound.centerY();
            int percent = (int) (((float) level / (float) mTotalProgress) * 100);// 中间的进度百分比，先转换成float在进行除法运算，不然都为0
            float textWidth = paint.measureText(percent + "%"); // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间
            if (percent != 0) {
                canvas.drawText(percent + "%", mXCenter - textWidth / 2, mYCenter + textSize / 2, paint); // 画出进度百分比
            }
//            Log.e(TAG,"percent = " + percent);
//            Log.e(TAG,"x = " + (mXCenter - textWidth / 2));
        }
    }

    @Override
    protected boolean onLevelChange(int level) {
        mProgress = level;
        if (level > 0 && level < 10000) {
            invalidateSelf();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mRingPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mRingPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return DrawableUtils.getOpacityFromColor(this.mRingPaint.getColor());
    }
}
