package com.awen.photo.photopick.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public class RoundProgressBarDrawable extends Drawable {
    // init Max value is 10000
    protected long mMaxValue = 10000;
    // The size of text
    private int mTextSize;
    // The color of text
    private int mTextColor;
    // the X offset of text
    private int mTextXOffset;
    private int mTextYOffset;
    //The text typeface
    private Typeface mTypeface;
    // THe visiable of text
    private boolean mTextShow;
    // The paint of text
    private Paint mTextPaint;
    // The Progress Value
    protected long mProgress;

    // The Paint of the Ring
    private Paint mCirclePaint;
    // The Size of the Ring
    private float mCircleWidth;
    // The progress Color for the Ring
    private int mCircleProgressColor;
    // The bottom color for the Ring
    private int mCircleBottomColor;
    //THe bottom circle width
    private int mCircleBottomWidth;
    // the radius of the Ring
    private int mCircleRadius;
    // Padding of line padding int the fan style
    private int mFanPadding;
    //Custom String
    private String mCustomStr;

    public RoundProgressBarDrawable() {
        this(false, 70);
    }

    public RoundProgressBarDrawable(boolean mTextShow, int mCircleRadius) {
        mTextPaint = new Paint();
        mTextSize = 20;
        mTextColor = Color.WHITE;
        this.mTextShow = mTextShow;
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mCirclePaint = new Paint();

        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStrokeWidth(10);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        mCircleProgressColor = 0xdddddddd;
        mCircleBottomColor = 0xdddddddd;
        mCircleWidth = 8;
        this.mCircleRadius = mCircleRadius;
        mFanPadding = 5;
        mCircleBottomWidth = 2;
        mProgress = 1;
    }

    /*
     * draw the circle background
     */
    private void drawCircle(Canvas canvas) {
        Rect bounds = getBounds();
        int xPos = bounds.left + bounds.width() / 2;
        int yPos = bounds.bottom - bounds.height() / 2;

        mCirclePaint.setColor(mCircleBottomColor);
        mCirclePaint.setStrokeWidth(mCircleBottomWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setShader(null);

        canvas.drawCircle(xPos, yPos, mCircleRadius + mFanPadding, mCirclePaint);
    }

    /*
     * draw the arc for the progress
     */
    private void drawArc(Canvas canvas) {
        mCirclePaint.setStyle(Paint.Style.FILL);
        Rect bounds = getBounds();
        int xpos = bounds.left + bounds.width() / 2;
        int ypos = bounds.bottom - bounds.height() / 2;
        RectF rectF = new RectF(xpos - mCircleRadius, ypos - mCircleRadius, xpos + mCircleRadius, ypos + mCircleRadius);
        float degree = (float) mProgress / (float) mMaxValue * 360;

        mCirclePaint.setStrokeWidth(mCircleWidth);
        mCirclePaint.setColor(mCircleProgressColor);
        canvas.drawArc(rectF, 270, degree, true, mCirclePaint);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if ((int) (((float) mProgress / (float) mMaxValue) * 100) == 100) {
            return;
        }
        drawCircle(canvas);
        drawArc(canvas);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    protected boolean onLevelChange(int level) {
        long origin = mProgress;
        mProgress = level;
        if (mProgress != 0 && origin != mProgress) {
            invalidateSelf();
            return true;
        } else {
            return false;
        }
    }

    public void setTextColor(int mTextColor) {
        mTextPaint.setColor(mTextColor);
        this.mTextColor = mTextColor;
    }

    public void setTextColorRes(@ColorRes int ColorRes, Context context) {
        mTextPaint.setColor(context.getResources().getColor(ColorRes));
    }

    public void setTextShow(boolean mTextShow) {
        this.mTextShow = mTextShow;
    }

    public void setTypeface(Typeface mTypeface) {
        this.mTypeface = mTypeface;
        mTextPaint.setTypeface(mTypeface);
    }

    public void setCustomText(String text) {
        mCustomStr = text;
    }

    public void setTextXOffset(int offset) {
        mTextXOffset = offset;
    }

    public void setTextYOffset(int offset) {
        mTextYOffset = offset;
    }

    public void setMaxValue(long value) {
        this.mMaxValue = value;
    }

    public void setmCircleWidth(float mCircleWidth) {
        this.mCircleWidth = mCircleWidth;
    }

    public void setmCircleBottomWidth(int mCircleBottomWidth) {
        this.mCircleBottomWidth = mCircleBottomWidth;
    }

    public void setmCircleRadius(int mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
    }
}
