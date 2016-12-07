package com.qicode.mylibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


import com.qicode.mylibrary.R;
import com.qicode.mylibrary.util.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kakaxicm on 2015/9/22.
 */
public class FlowLayout extends ViewGroup {
    private final int DIP_ITEM_GAP = 5;
    private int topGap = (int) SizeUtils.dp2Px(getResources(), DIP_ITEM_GAP);
    private int leftGap = (int) SizeUtils.dp2Px(getResources(), DIP_ITEM_GAP);
    private int bottomGap = (int) SizeUtils.dp2Px(getResources(), DIP_ITEM_GAP);
    private int rightGap = (int) SizeUtils.dp2Px(getResources(), DIP_ITEM_GAP);

    private int mCurrentSelectedIndex;
    //tag点击事件
    public interface OnFlowTagOnSelectedListener{
        /**
         * @param section flow所在的组,也就是自己的tag
         * @param pos  该组下面的item的tag
         */
        void onFlowTagSelected(int section, int pos);
    }

    private OnFlowTagOnSelectedListener mTagSelectedListener;

    public void setTagSelectedListener(OnFlowTagOnSelectedListener listener) {
        mTagSelectedListener = listener;
    }

    /**
     * 存储所有的View，按行记录
     */
    private List<List<View>> mAllViews = new ArrayList<>();
    /**
     * 记录每一行的最大高度
     */
    private List<Integer> mLineHeight = new ArrayList<>();

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }


    /**
     * 根据子空间设置自己的宽和高
     * Step1 获得每个Child的宽高和Margin
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获得父容器为他设置的模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // 如果是warp_content情况下，记录宽和高
        int width = 0;
        int height = 0;

        /**
         * 记录每一行的宽度，width不断取最大宽度
         */
        int lineWidth = 0;

        /**
         * 每一行的高度，累加至height
         */
        int lineHeight = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            //Step1 获得每个Child的宽高和Margin
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            // 得到child的lp
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            //当前子控件占据的宽高
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (lineWidth + childWidth <= sizeWidth) {//未换行,累加宽度，更新行高
                lineWidth += childWidth;
                lineHeight = Math.max(childHeight, lineHeight);
            } else {//换行,宽度取最大值，高度累加
                width = Math.max(lineWidth, childWidth);
                height += lineHeight;//叠加上一行高度
                lineWidth = childWidth;//开启新行
                lineHeight = childHeight;
            }

            //到最后一个，把最后一行的宽高处理下,exp:两次换行，实际是3行
            if (i == count - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        // 存储每一行所有的childView
        List<View> lineViews = new ArrayList<>();
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            //需要换行,记录这一行的所有View及最大高度
            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width) {
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
                lineWidth = 0;//重置行宽
                lineHeight = 0;//重置行高
                lineViews = new ArrayList<>();
            }

            /**
             * 不需要换行，则累加
             */
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(childHeight + lp.topMargin + lp.bottomMargin, lineHeight);
            lineViews.add(child);
        }

        //记录最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);
        /**
         * 布局的起点
         */
        int left = 0;
        int top = 0;

        //按行布局
        int lineNumbers = mAllViews.size();
        for (int i = 0; i < lineNumbers; i++) {
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }

            //下一行开始布局
            left = 0;
            top += lineHeight;
        }

    }

    public void setMargin(int top, int left, int bottom, int right) {
        topGap = top;
        leftGap = left;
        bottomGap = bottom;
        rightGap = right;
    }

    @Override
    public void addView(View child) {
        MarginLayoutParams lp = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
        lp.bottomMargin = bottomGap;
        lp.topMargin = topGap;
        lp.leftMargin = leftGap;
        lp.rightMargin = rightGap;
        child.setBackgroundResource(R.drawable.bg_button_gray);
        child.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int section = (int) getTag();
                int pos = (int) v.getTag();

                //点击事件的bg处理
                if(mTagSelectedListener != null) {
                    mTagSelectedListener.onFlowTagSelected(section, pos);
                }
                updateItemBg(mCurrentSelectedIndex, pos);
                mCurrentSelectedIndex = pos;
            }
        });
        super.addView(child, lp);
    }

    //更新选中的item背景
    public void updateItemBg(int preSelectedIndex, int selectIndex) {

        View preSelectedChild = getChildAt(preSelectedIndex);//上次选中的view
        View curSelectedChild = getChildAt(selectIndex);//当前选中的view

        preSelectedChild.setBackgroundResource(R.drawable.bg_button_gray);
        curSelectedChild.setBackgroundResource(R.drawable.bg_button_orange);
    }

    //复位选中状态
    public void clearSelectedStates(){
        mCurrentSelectedIndex = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setBackgroundResource(R.drawable.bg_button_gray);
        }
    }
}
