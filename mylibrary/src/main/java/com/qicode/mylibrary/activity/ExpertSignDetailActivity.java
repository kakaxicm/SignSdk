package com.qicode.mylibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.qicode.mylibrary.R;
import com.qicode.mylibrary.adapter.ExpertSignDetailAdapter;
import com.qicode.mylibrary.constant.AppConstant;
import com.qicode.mylibrary.constant.NetConstant;
import com.qicode.mylibrary.model.SignDetailResponse;
import com.qicode.mylibrary.util.ActivityUtils;
import com.qicode.mylibrary.util.NetUtils;

/**
 * Created by star on 16/3/14.
 */
public class ExpertSignDetailActivity extends BaseActivity {
    //View
    private RecyclerView mImagesRcv;//图片列表
    private TextView mProductNameView;//标题

    //Adapter
    private ExpertSignDetailAdapter mAdapter;

    //数据
    private int mExpertSignId;
    private String mName;

    private int mDesignPrice;
    private int mVideoPrice;

    private SignDetailResponse mSignDetailData;

    private View mLoadStateContainer;//加载状态View
    private CircleProgressBar mLoadingProgressBar;//loading pb
    private View mLoadFailedContgainer;//加载失败UI
    private Button mRetryBtn;//重试按钮

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_expert_sign_detail;
    }

    @Override
    protected void getIntentData() {
        mExpertSignId = getIntent().getIntExtra(AppConstant.INTENT_EXPERT_SIGN_ID, 0);
        mName = getIntent().getStringExtra(AppConstant.INTENT_NAME);

        mDesignPrice = getIntent().getIntExtra(AppConstant.INTENT_DESIGN_PRICE, 0);
        mVideoPrice = getIntent().getIntExtra(AppConstant.INTENT_VIDEO_PRICE, 0);
    }

    @Override
    protected void initTitle() {
        mProductNameView = (TextView) findViewById(R.id.tv_left_title);
        findViewById(R.id.iv_right).setVisibility(View.GONE);
        setOnClickListener(findViewById(R.id.iv_left));
    }

    @Override
    protected void initContent() {
        mImagesRcv = (RecyclerView) findViewById(R.id.rv_image);
        //适配器
        mAdapter = new ExpertSignDetailAdapter(mContext);
        mImagesRcv.setAdapter(mAdapter);

        LinearLayoutManager lm = new LinearLayoutManager(mContext);

        mImagesRcv.setLayoutManager(lm);

        View designView = findViewById(R.id.tv_designing);
        setOnClickListener(designView);
        initLoadStateView();
    }

    /**
     *初始化加载状态View, 包括加载中和加载失败
     */
    protected void initLoadStateView() {
        mLoadStateContainer = findViewById(R.id.load_state_container);
        mLoadFailedContgainer = mLoadStateContainer.findViewById(R.id.ll_load_failed_container);
        mRetryBtn = (Button) mLoadStateContainer.findViewById(R.id.btn_retry);
        mRetryBtn.setOnClickListener(this);//重试点击事件处理
        mLoadingProgressBar = (CircleProgressBar) mLoadStateContainer.findViewById(R.id.loading_progressbar);
        mLoadingProgressBar.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light,
                android.R.color.holo_green_light, android.R.color.holo_red_light);
    }


    @Override
    protected void attachData() {
        showLoadingProgressBar();
        mHandlerList.add(NetUtils.getInstance().postNocache(mContext, NetConstant.URL.SIGN_DETAIL_URL.getUrl(mContext),
                NetConstant.getSignDetailParams(mContext, mExpertSignId), new ExpertSignDetailListener(mActivity)));
    }

    //新的专家签名回调
    private class ExpertSignDetailListener extends NetUtils.Callback<SignDetailResponse> {

        public ExpertSignDetailListener(Activity activity) {
            super(activity, SignDetailResponse.class);
        }

        @Override
        public void onNetSuccess(SignDetailResponse response) {
            mSignDetailData = response;
            onReceiverData();
            showRcv();
        }

        @Override
        public void onNetFailed(HttpException e) {
            showLoadFailedUi(e.getLocalizedMessage());
        }
    }

    private void onReceiverData() {
        if(mSignDetailData !=null && mSignDetailData.getStatus().getCode().equals("0")) {
            mProductNameView.setText(mSignDetailData.getResult().getSign_name());
            mAdapter.setData(mSignDetailData);
        }
    }

    /**
     * 显示pb
     */
    protected void showLoadingProgressBar(){
        mImagesRcv.setVisibility(View.VISIBLE);
        mLoadFailedContgainer.setVisibility(View.GONE);
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 显示加载失败的UI
     */
    protected void showLoadFailedUi(String error){
        mImagesRcv.setVisibility(View.INVISIBLE);
        mLoadFailedContgainer.setVisibility(View.VISIBLE);
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示rcv
     */
    protected void showRcv(){
        mImagesRcv.setVisibility(View.VISIBLE);
        mLoadFailedContgainer.setVisibility(View.INVISIBLE);
        mLoadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        int id = view.getId();
        if(id == R.id.tv_designing){
            intent = new Intent(mContext, SignPayActivity.class);
            intent.putExtra(AppConstant.INTENT_EXPERT_SIGN_ID, mExpertSignId);
            intent.putExtra(AppConstant.INTENT_NAME, mName);
            //价格
            intent.putExtra(AppConstant.INTENT_DESIGN_PRICE, mDesignPrice);
            intent.putExtra(AppConstant.INTENT_VIDEO_PRICE, mVideoPrice);

            ActivityUtils.jump(mContext, intent);
            return;
        }

        if(id == R.id.iv_left){
            finish();
            return;
        }

        if(id == R.id.btn_retry){
            attachData();
            return;
        }

    }

}
