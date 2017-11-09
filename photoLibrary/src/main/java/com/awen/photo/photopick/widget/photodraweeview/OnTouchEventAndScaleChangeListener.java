package com.awen.photo.photopick.widget.photodraweeview;

import android.graphics.Matrix;
import android.view.MotionEvent;

/**
 * 解决多指缩放和下拉冲突的,该接口主要用于用于ScalePhotoView，因为PhotoDraweeView它的多指操作，原图缩小的时候,onTouch会被我这里中断
 * Created by Awen <Awentljs@gmail.com>
 */

public interface OnTouchEventAndScaleChangeListener {

    void onPhotoTouchEvent(MotionEvent ev);

    void onPhotoScaleChange(float matrixScale);

    void onPhotoScaleEnd(float matrixScale);
}
