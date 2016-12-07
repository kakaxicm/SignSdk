package com.qicode.mylibrary.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import java.util.ArrayList;


/**
 * Created by xinmei on 2014/7/29.
 */
public class AnimatorUtil {
    private static AnimationSet mAnimationSet;

    static {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, 0.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
        mAnimationSet = new AnimationSet(true);
        mAnimationSet.addAnimation(scaleAnimation);
        mAnimationSet.addAnimation(alphaAnimation);
        mAnimationSet.setInterpolator(new OvershootInterpolator());
        mAnimationSet.setDuration(ViewConfiguration.getLongPressTimeout() / 2);
    }

    public static void startPopupAnimation(View targetView) {
        targetView.startAnimation(mAnimationSet);
    }

    /**
     * 心跳效果
     */
    public static void startHeartBeat(View targetView) {
        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.4f, 1.0f);
        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.4f, 1.0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(targetView, holderX, holderY);
        animator.setDuration(500);
        animator.setInterpolator(new OvershootInterpolator());
        animator.start();
    }

    /**
     * 心跳效果
     */
    public static void startLittleHeartBeat(View targetView, Animator.AnimatorListener listener) {
        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.1f, 1.0f);
        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.1f, 1.0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(targetView, holderX, holderY);
        animator.setDuration(500);
        animator.setInterpolator(new OvershootInterpolator());
        if (listener != null) {
            animator.addListener(listener);
        }
        animator.start();
    }

    public static void startShrinkAnimator(View targetView) {
        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.95f, 1.0f);
        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.95f, 1.0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(targetView, holderX, holderY);
        animator.setDuration(500);
        animator.setInterpolator(new OvershootInterpolator());
        animator.start();
    }

    public static void repeatShrinkAnimator(View targetView) {
        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("rotation", 0.0f, 15.0f, -15.0f, 0.0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(targetView, holderX);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.setDuration(500);
        animator.setInterpolator(new OvershootInterpolator());
        animator.start();
    }

    public static ObjectAnimator createRotateAnimator(View targetView, float fromRotateZ, float endRotateZ) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "rotation", fromRotateZ, endRotateZ);
        animator.setDuration(500);
        return animator;
    }

    public static ObjectAnimator createTranslationAnimator(View targetView, float fromX, float endX, float fromY, float endY) {
        PropertyValuesHolder phX = PropertyValuesHolder.ofFloat("translationX", fromX, endX);
        PropertyValuesHolder phY = PropertyValuesHolder.ofFloat("translationY", fromY, endY);
        return ObjectAnimator.ofPropertyValuesHolder(targetView, phX, phY);
    }

    public static ObjectAnimator createAlphaAnimator(View targetView, float fromAlpha, float endAlpha) {
        return ObjectAnimator.ofFloat(targetView, "alpha", fromAlpha, endAlpha);
    }

    public static ObjectAnimator createDelayAlphaSplashAnimator(View targetView, long delay) {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.3f, 1.0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(targetView, alpha);
        animator.setDuration(500);
        animator.setStartDelay(delay);
        return animator;
    }

    public static ObjectAnimator createScaleAnimator(View targetView, float fromScale, float endScale) {
        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("scaleX", fromScale, endScale);
        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("scaleY", fromScale, endScale);
        return ObjectAnimator.ofPropertyValuesHolder(targetView, holderX, holderY);
    }

    public static void showControlView(final View parent, View[] children, final View[] subChildren) {
        ArrayList<Animator> childrenAnimator = new ArrayList<>();
        ArrayList<Animator> subChildrenAnimator = new ArrayList<>();
        int subChildWidth = (int) SizeUtils.dp2Px(parent.getContext().getResources(), 180.0f);
        // 子View添加靠右对齐,并开启动画
        for (int i = 0; i < children.length; i++) {
            View child = children[i];
            RelativeLayout.LayoutParams childLayoutParams = (RelativeLayout.LayoutParams) child.getLayoutParams();
            childLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            PropertyValuesHolder phX = PropertyValuesHolder.ofFloat("translationX", child.getTranslationX(), -subChildWidth * i);
            childrenAnimator.add(ObjectAnimator.ofPropertyValuesHolder(child, phX).setDuration(400));
        }
        // 子子view扩大、出现、下降，并开启动画
        for (View subChild : subChildren) {
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", subChild.getScaleX(), 1);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", subChild.getScaleY(), 1);
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", subChild.getAlpha(), 1);
            PropertyValuesHolder translationY =
                    PropertyValuesHolder.ofFloat("translationY", subChild.getTranslationY(), 0);
            subChildrenAnimator.add(ObjectAnimator.ofPropertyValuesHolder(subChild, scaleX, scaleY, alpha, translationY).setDuration(400));
        }
        // 启动子View动画
        AnimatorSet childrenAnimatorSet = new AnimatorSet();
        childrenAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                // 控制面板全屏
                RelativeLayout.LayoutParams parentLayoutParams = (RelativeLayout.LayoutParams) parent.getLayoutParams();
                parentLayoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                parentLayoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                parent.setLayoutParams(parentLayoutParams);
            }
        });
        childrenAnimatorSet.playTogether(childrenAnimator);
        childrenAnimatorSet.start();
        // 启动子子View动画
        AnimatorSet subChildrenAnimatorSet = new AnimatorSet();
        subChildrenAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                // 显示所有子子view
                for (View subChild : subChildren) {
                    subChild.setVisibility(View.VISIBLE);
                }
            }
        });
        subChildrenAnimatorSet.playTogether(subChildrenAnimator);
        subChildrenAnimatorSet.setStartDelay(400);
        subChildrenAnimatorSet.start();
    }

    public static void hideControlView(final View parent, View[] children, final View[] subChildren) {
        ArrayList<Animator> childrenAnimator = new ArrayList<>();
        ArrayList<Animator> subChildrenAnimator = new ArrayList<>();
        // 子View移除靠右对齐,并开启动画
        for (View child : children) {
            RelativeLayout.LayoutParams childLayoutParams = (RelativeLayout.LayoutParams) child.getLayoutParams();
            childLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            PropertyValuesHolder phX =
                    PropertyValuesHolder.ofFloat("translationX", child.getTranslationX(), -SizeUtils.dp2Px(parent.getResources(), 0));
            childrenAnimator.add(ObjectAnimator.ofPropertyValuesHolder(child, phX).setDuration(400));
        }
        // 子子view缩小、消失、上升，并开启动画
        for (View subChild : subChildren) {
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", subChild.getScaleX(), 0);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", subChild.getScaleY(), 0);
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", subChild.getAlpha(), 0);
            PropertyValuesHolder translationY = PropertyValuesHolder
                    .ofFloat("translationY", subChild.getTranslationY(), -SizeUtils.dp2Px(parent.getContext().getResources(), 40));
            subChildrenAnimator.add(ObjectAnimator.ofPropertyValuesHolder(subChild, scaleX, scaleY, alpha, translationY).setDuration(400));
        }
        // 启动子View动画
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 控制面板缩小
                RelativeLayout.LayoutParams parentLayoutParams = (RelativeLayout.LayoutParams) parent.getLayoutParams();
                parentLayoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                parentLayoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                parent.setLayoutParams(parentLayoutParams);
            }
        });
        animatorSet.playTogether(childrenAnimator);
        animatorSet.setStartDelay(400);
        animatorSet.start();
        // 启动子子View动画
        AnimatorSet subAnimatorSet = new AnimatorSet();
        subAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 显示所有子子view
                for (View subChild : subChildren) {
                    subChild.setVisibility(View.GONE);
                }
            }
        });
        subAnimatorSet.playTogether(subChildrenAnimator);
        subAnimatorSet.start();
    }
}
