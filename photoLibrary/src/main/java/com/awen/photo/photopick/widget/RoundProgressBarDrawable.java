package com.awen.photo.photopick.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Awen <Awentljs@gmail.com>
 */

public class RoundProgressBarDrawable extends Drawable {
    private long maxValue = 10000;
    private long progress;
    private Paint circlePaint;
    private float circleWidth;
    private int circleProgressColor;
    private int circleBottomColor;
    private int circleBottomWidth;
    private int circleRadius;
    private int interval;//两个圆之间的间隔

    public RoundProgressBarDrawable() {
        this(70);
    }

    public RoundProgressBarDrawable(int circleRadius) {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(10);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);
        circleProgressColor = 0xdddddddd;
        circleBottomColor = 0xdddddddd;
        circleWidth = 8;
        this.circleRadius = circleRadius;
        interval = 5;
        circleBottomWidth = 2;
        progress = 1;
    }

    /*
     * 话空心圆
     */
    private void drawCircle(Canvas canvas) {
        Rect bounds = getBounds();
        int xPos = bounds.left + (bounds.width() >> 1);
        int yPos = bounds.bottom - (bounds.height() >> 1);
        circlePaint.setColor(circleBottomColor);
        circlePaint.setStrokeWidth(circleBottomWidth);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setShader(null);
        canvas.drawCircle(xPos, yPos, circleRadius + interval, circlePaint);
    }

    /*
     * 根据进度条画实心圆
     */
    private void drawArc(Canvas canvas) {
        circlePaint.setStyle(Paint.Style.FILL);
        Rect bounds = getBounds();
        int xPos = bounds.left + (bounds.width() >> 1);
        int yPos = bounds.bottom - (bounds.height() >> 1);
        RectF rectF = new RectF(xPos - circleRadius, yPos - circleRadius, xPos + circleRadius, yPos + circleRadius);
        float degree = (float) progress / (float) maxValue * 360;
        circlePaint.setStrokeWidth(circleWidth);
        circlePaint.setColor(circleProgressColor);
        canvas.drawArc(rectF, 270, degree, true, circlePaint);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if ((int) (((float) progress / (float) maxValue) * 100) == 100) {
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
        return PixelFormat.UNKNOWN;
    }

    @Override
    protected boolean onLevelChange(int level) {
        long origin = progress;
        progress = level;
        if (progress != 0 && origin != progress) {
            invalidateSelf();
            return true;
        } else {
            return false;
        }
    }

    public RoundProgressBarDrawable setMaxValue(long value) {
        this.maxValue = value;
        return this;
    }

    public RoundProgressBarDrawable setCircleWidth(float circleWidth) {
        this.circleWidth = circleWidth;
        return this;
    }

    public RoundProgressBarDrawable setCircleBottomWidth(int circleBottomWidth) {
        this.circleBottomWidth = circleBottomWidth;
        return this;
    }

    public RoundProgressBarDrawable setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
        return this;
    }

    public RoundProgressBarDrawable setInterval(int interval) {
        this.interval = interval;
        return this;
    }
}
