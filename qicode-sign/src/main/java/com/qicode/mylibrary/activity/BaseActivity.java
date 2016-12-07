package com.qicode.mylibrary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.lidroid.xutils.http.HttpHandler;
import com.qicode.mylibrary.R;
import com.qicode.mylibrary.constant.AppConstant;
import com.qicode.mylibrary.util.DialogUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = this;
        getIntentData();
        initBaseData();
        setContentView();
        initTitle();
        initContent();
        attachData();
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
