package com.awen.photo.photopick.widget.clipimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

/**
 * 裁剪头像整个layout
 * 
 * @author Homk-M <Awentljs@gmail.com>
 * 
 */
public class ClipImageLayout extends RelativeLayout {

	private ClipZoomImageView mZoomImageView;

	/**
	 * 可以提取为自定义属性
	 */
	private int mHorizontalPadding = 20;

	public ClipImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public void init(Context context, Bitmap bitmap) {
		mZoomImageView = new ClipZoomImageView(context);
		ClipImageBorderView mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		mZoomImageView.setImageBitmap(bitmap);

		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);

		// 计算padding的px
		mHorizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
				.getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setHorizontalPadding(mHorizontalPadding);
	}

	/**
	 * 对外公布设置边距的方法,单位为dp
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
	}

	/**
	 * 裁切图片
	 * 
	 * @return
	 */
	public Bitmap clip() {
		return mZoomImageView.clip();
	}

}
