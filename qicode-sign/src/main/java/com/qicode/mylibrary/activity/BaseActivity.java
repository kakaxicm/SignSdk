package com.qicode.mylibrary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lidroid.xutils.http.HttpHandler;
import com.qicode.mylibrary.R;
import com.qicode.mylibrary.constant.AppConstant;
import com.qicode.mylibrary.util.DialogUtils;
import com.qicode.mylibrary.util.SizeUtils;
import com.qicode.mylibrary.widget.StatusBarView;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public abstract class BaseActivity extends SwipeBackActivity implements OnClickListener {
    protected static final String TAG = BaseActivity.class.getSimpleName();
    private static final int DoubleBackTime = 2000;
    // 页面根节点
    protected View mRootLayout;

    protected List<HttpHandler> mHandlerList;

    protected boolean isNeedDoubleBack;
    private long mKeyTime;
    private int mKeyCount;

    protected Activity mActivity;
    protected Context mContext;

    int THEME_COLOR = Color.parseColor("#54b0fe");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = this;
        getIntentData();
        initBaseData();
        setContentView();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){//5.0以上
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(THEME_COLOR);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){//4.4-5.0
            immerseStatusBar();
        }
        initTitle();
        initContent();
        attachData();
    }

    /**
     * 沉浸状态栏处理
     */
    private void immerseStatusBar(){
        //获取windowphone下的decorView
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        int count = decorView.getChildCount();
        //判断是否已经添加了statusBarView
        if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
            decorView.getChildAt(count - 1).setBackgroundColor(THEME_COLOR);
        } else {
            //新建一个和状态栏高宽的view
            StatusBarView statusView = createStatusBarView(THEME_COLOR);
            decorView.addView(statusView);
        }
        ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        //rootview不会为状态栏留出状态栏空间
        ViewCompat.setFitsSystemWindows(rootView, true);
        rootView.setClipToPadding(true);
    }

    /**
     * 创建占位View
     * @param color
     * @return
     */
    private StatusBarView createStatusBarView(int color) {
        // 绘制一个和状态栏一样高的矩形
        StatusBarView statusBarView = new StatusBarView(this);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.getStatusBarHeight(this));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    @Override
    protected void onResume() {
        hideFloatView();
        refreshData();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        for (HttpHandler<String> handler : mHandlerList) {
            if (handler != null && !handler.isCancelled()) {
                handler.cancel();
            }
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onBackPressed() {
        if (isNeedDoubleBack) {
            long current = System.currentTimeMillis();
            if (current - mKeyTime > DoubleBackTime) {
                DialogUtils.showShortPromptToast(mContext, R.string.click_again_to_exit);
            } else {
                super.onBackPressed();
            }
            mKeyTime = current;
        } else {
            super.onBackPressed();
        }
    }

    private void initBaseData() {
        isNeedDoubleBack = false;
        mHandlerList = new ArrayList<>();
        initData();
    }

    private void setContentView() {
        // 初始化页面布局
        int res = setLayoutViewId();
        if (res != 0) {
            mRootLayout = LayoutInflater.from(this).inflate(setLayoutViewId(), null);
            setContentView(mRootLayout);
        }
    }

    /**
     * 设置页面布局
     */
    protected abstract int setLayoutViewId();

    /**
     * 获取intent数据
     */
    protected void getIntentData() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 初始化title
     */
    protected void initTitle() {
    }

    /**
     * 初始化内容布局
     */
    protected void initContent() {
    }

    /**
     * 为view控件绑定数据
     */
    protected void attachData() {
    }

    /**
     * 刷新数据
     */
    protected void refreshData() {
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

    /**
     * 统一为各种view确定可以点击
     */
    protected void setClickEnable(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setEnabled(true);
            }
        }
    }

    /**
     * 统一为各种view确定不可以点击
     */
    protected void setClickDisable(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setEnabled(false);
            }
        }
    }

    /**
     * 统一为各种view确定可见状态
     */
    protected void setViewVisible(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 统一为各种view确定不可见状态
     */
    protected void setViewInvisible(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 统一为各种view确定不可见状态
     */
    protected void setViewGone(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.GONE);
            }
        }
    }

//    /**
//     * 用于显示调试信息
//     */
//    protected void showDebugInfo(Context context) {
//        if ((System.currentTimeMillis() - mKeyTime) > 500) {
//            mKeyCount = 0;
//        } else {
//            mKeyCount++;
//            if (mKeyCount > 10) {
//                DialogUtils.showDebugInfoDialog(context);
//            }
//        }
//        mKeyTime = System.currentTimeMillis();
//    }

    /**
     * 跳转到activity
     */
    protected void jump(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.pull_right_out);
    }

    /**
     * 跳转到activity
     */
    protected void jump(Context context, Class<?> targetClass) {
        Intent intent = new Intent();
        intent.setClass(context, targetClass);
        jump(intent);
    }

    /**
     * 跳转到activity
     */
    protected void jump(Context context, Class<?> targetClass, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, targetClass);
        overridePendingTransition(R.anim.push_left_in, R.anim.pull_right_out);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 隐藏浮动图标
     */
    private void hideFloatView() {
        Intent serviceIntent = new Intent();
        serviceIntent.setAction(getApplication().getPackageName());
        serviceIntent.putExtra(AppConstant.INTENT_FLOAT_VIEW_ACTION, AppConstant.INTENT_HIDE_FLOAT_VIEW);
        sendBroadcast(serviceIntent);
    }

    /**
     * 显示浮动图标
     */
    private void showFloatView() {
        Intent serviceIntent = new Intent();
        serviceIntent.setAction(getApplication().getPackageName());
        serviceIntent.putExtra(AppConstant.INTENT_FLOAT_VIEW_ACTION, AppConstant.INTENT_SHOW_FLOAT_VIEW);
        sendBroadcast(serviceIntent);
    }

}
