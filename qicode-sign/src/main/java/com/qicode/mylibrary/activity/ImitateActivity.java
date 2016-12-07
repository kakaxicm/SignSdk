package com.qicode.mylibrary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.qicode.mylibrary.R;
import com.qicode.mylibrary.constant.AppConstant;
import com.qicode.mylibrary.util.AnimatorUtil;
import com.qicode.mylibrary.util.SizeUtils;
import com.rey.material.widget.FloatingActionButton;

public class ImitateActivity extends Activity implements View.OnClickListener{
    private String mName;
    private String mImageUrl;

    private SimpleDraweeView mSignImageView;
    private TextView mSignTextView;
    private SignaturePad mPadView;
    private View mControlView;
    private FloatingActionButton mMoreView;
    private View mPaintSizeView;
    private View mPaintSizeChooseView;
    private View mPaintColorView;
    private View mPaintColorChooseView;

    private ControlState mControlState;
    private Context mContext;

    private enum ControlState {
        Extend(0), Shrink(1);

        private int state;

        ControlState(int state) {
            this.state = state;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initData();
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(setLayoutViewId());
        getIntentData();
        initContent();
        attachData();
    }

    protected void getIntentData() {
        Intent intent = getIntent();
        mName = intent.getStringExtra(AppConstant.INTENT_NAME);
        mImageUrl = intent.getStringExtra(AppConstant.INTENT_IMAGE_URL);
    }

    protected void initData() {
        mControlState = ControlState.Extend;
        // 隐藏android系统的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected int setLayoutViewId() {
        return R.layout.activity_imitate;
    }

    protected void initContent() {
        Log.e("Activity", "ImitateActivity ---- OnCreate");
        // 签名
        mSignImageView = (SimpleDraweeView) findViewById(R.id.iv_sign);
        mSignTextView = (TextView) findViewById(R.id.tv_sign);
        mPadView = (SignaturePad) findViewById(R.id.signature_pad);
        ViewGroup.LayoutParams params = mSignImageView.getLayoutParams();
        params.width = SizeUtils.getScreenWidth(mContext);
        params.height = SizeUtils.getScreenHeight(mContext);
        mSignImageView.setLayoutParams(params);
        // 控制按钮
        View exitView = findViewById(R.id.fab_exit);
        View restartView = findViewById(R.id.fab_restart);
        mControlView = findViewById(R.id.rl_control);
        mMoreView = (FloatingActionButton) findViewById(R.id.fab_more);
        mPaintSizeView = findViewById(R.id.fab_paint_size);
        mPaintSizeChooseView = findViewById(R.id.ll_paint_size);
        mPaintColorView = findViewById(R.id.fab_paint_color);
        mPaintColorChooseView = findViewById(R.id.ll_paint_color);
        // 画笔
        View paintPinkView = findViewById(R.id.iv_paint_color_pink);
        View paintBlueView = findViewById(R.id.iv_paint_color_blue);
        View paintGreenView = findViewById(R.id.iv_paint_color_green);
        View paintOrangeView = findViewById(R.id.iv_paint_color_orange);
        View paintGrayView = findViewById(R.id.iv_paint_color_gray);
        View paintSmallerView = findViewById(R.id.iv_paint_size_smaller);
        View paintSmallView = findViewById(R.id.iv_paint_size_small);
        View paintNormalView = findViewById(R.id.iv_paint_size_normal);
        View paintBigView = findViewById(R.id.iv_paint_size_big);
        View paintBiggerView = findViewById(R.id.iv_paint_size_bigger);

        setOnClickListener(mMoreView, mControlView, exitView, restartView, paintPinkView, paintBlueView, paintGreenView,
                paintOrangeView,
                paintGrayView, paintSmallerView, paintSmallView, paintNormalView, paintBigView, paintBiggerView);
    }

    /**
     * 统一为各种view添加点击事件
     */
    protected void setOnClickListener(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(this);
            }
        }
    }

    protected void attachData() {
        mSignImageView.setImageURI(Uri.parse(mImageUrl));
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.fab_exit){
            finish();
        }else if(id == R.id.fab_restart){
            mPadView.clear();
        }else if(id == R.id.fab_more){
            changeControlPanel(mControlState);
        }else if(id == R.id.rl_control){
            changeControlPanel(ControlState.Shrink);
        }else if(id == R.id.iv_paint_color_pink){
            mPadView.setPenColor(getResources().getColor(R.color.paint_pink));
            changeControlPanel(ControlState.Shrink);
        }else if(id == R.id.iv_paint_color_blue){
            mPadView.setPenColor(getResources().getColor(R.color.paint_blue));
            changeControlPanel(ControlState.Shrink);
        }else if(id == R.id.iv_paint_color_green){
            mPadView.setPenColor(getResources().getColor(R.color.paint_green));
            changeControlPanel(ControlState.Shrink);
        }else if(id == R.id.iv_paint_color_orange){
            mPadView.setPenColor(getResources().getColor(R.color.paint_orange));
            changeControlPanel(ControlState.Shrink);
        }else if(id == R.id.iv_paint_color_gray){
            mPadView.setPenColor(getResources().getColor(R.color.paint_gray));
            changeControlPanel(ControlState.Shrink);
        }else if(id == R.id.iv_paint_size_smaller){
            mPadView.setMaxWidth(12.0f);
            mPadView.setMinWidth(3.0f);
            changeControlPanel(ControlState.Shrink);
        }else if(id == R.id.iv_paint_size_small){
            mPadView.setMaxWidth(15.0f);
            mPadView.setMinWidth(6.0f);
            changeControlPanel(ControlState.Shrink);
        }else if(id == R.id.iv_paint_size_normal){
            mPadView.setMaxWidth(18.0f);
            mPadView.setMinWidth(9.0f);
            changeControlPanel(ControlState.Shrink);
        }else if(id == R.id.iv_paint_size_big){
            mPadView.setMaxWidth(21.0f);
            mPadView.setMinWidth(12.0f);
            changeControlPanel(ControlState.Shrink);
        }else if(id == R.id.iv_paint_size_big){
            mPadView.setMaxWidth(21.0f);
            mPadView.setMinWidth(12.0f);
            changeControlPanel(ControlState.Shrink);
        }else if(id == R.id.iv_paint_size_bigger){
            mPadView.setMaxWidth(24.0f);
            mPadView.setMinWidth(15.0f);
            changeControlPanel(ControlState.Shrink);
        }
    }

    private void changeControlPanel(ControlState state) {
        switch (state) {
            case Extend:
                // 启动展开控制面板动画
                AnimatorUtil.showControlView(mControlView, new View[]{mMoreView, mPaintSizeView, mPaintColorView},
                        new View[]{mPaintColorChooseView, mPaintSizeChooseView});
                // 更新控制按钮状态
                mControlState = ControlState.Shrink;
                break;
            case Shrink:
                // 启动收缩控制面板动画
                AnimatorUtil.hideControlView(mControlView, new View[]{mMoreView, mPaintSizeView, mPaintColorView},
                        new View[]{mPaintColorChooseView, mPaintSizeChooseView});
                // 更新控制按钮状态
                mControlState = ControlState.Extend;
                break;
        }
        //刷新更多按钮的状态
        mMoreView.setLineMorphingState(mControlState.state, true);
    }

}
