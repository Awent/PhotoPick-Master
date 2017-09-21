package com.awen.photo.photopick.anim;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;

public class AnimListener implements Animator.AnimatorListener {
    private View mFromView;
    private View mToView;

    public AnimListener(View fromView, View toView) {
        mFromView = fromView;
        mToView = toView;
    }

    @Override
    public void onAnimationStart(Animator animator) {
        mFromView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        mFromView.setVisibility(View.INVISIBLE);
        ((ViewGroup) mFromView.getParent()).removeView(mFromView);

        if (mToView != null) {
            mToView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}