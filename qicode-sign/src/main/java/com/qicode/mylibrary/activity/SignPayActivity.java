package com.qicode.mylibrary.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.pingplusplus.android.PaymentActivity;
import com.qicode.mylibrary.R;
import com.qicode.mylibrary.constant.AppConstant;
import com.qicode.mylibrary.constant.NetConstant;
import com.qicode.mylibrary.model.ChargeResponse;
import com.qicode.mylibrary.model.PriceResponse;
import com.qicode.mylibrary.model.SignDetailResponse;
import com.qicode.mylibrary.util.DialogUtils;
import com.qicode.mylibrary.util.NetUtils;
import com.qicode.mylibrary.util.StringUtils;
import com.qicode.mylibrary.util.UserInfoUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by star on 15/10/23.
 */
public class SignPayActivity extends BaseActivity implements DialogInterface.OnClickListener {
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_SELECT_SELECT_EN_NAME = 3;
    private static final int REQUEST_CODE_LOGIN = 4;

    private EditText mNameView;
    private EditText mRequirementEt;
    private SimpleDraweeView mHeaderView;
    private TextView mProductNameView;
    private TextView mPriceView;
    private TextView mTermView;
    private TextView mDelayTimeDescView;
    private View mPayView;
    private TextView mResultPriceTv;
    private TextView mPreferentialPriceTv;//优惠价格

    private CheckBox mIsVideoCheckBox;//是否包含视频教程
    private TextView mVideoPriceTv;

    private int mExpertSignId;
    private String mPayMethod = AppConstant.CHANNEL_ALIPAY;
    private String mName;
    private String mNameFormatReg;//正则表达式格式
    private String mNameFormatDesc;//姓名正确格式描述
    private String mRequirement;
    private String mProductType = "2";//1.设计图 2.设计图+视频(默认勾选) 3.追加视频

    private boolean mIsPayActioned;

    private int mDesignPrice;//设计稿价格
    private int mVideoPrice;//视频价格
    private int mPreferential;//优惠价格
    private int mTotalPrice;//手稿价格+视频价格-优惠价格,有视频的情况下会有优惠价格
    private int mFinalPrice;//最终价格
    private boolean mIsAppendVideo;//是否追加视频,如果追加,则订单类型为3， 不显示设计稿
    private int mUserSignId;//为哪一个用户签名追加ID

    private View mSelectEnNameEntry;

    private View mContentView;
    private View mLoadStateContainer;//加载状态View
    private CircleProgressBar mLoadingProgressBar;//loading pb
    private View mLoadFailedContgainer;//加载失败UI
    private Button mRetryBtn;//重试按钮

    @Override
    protected void getIntentData() {
        mExpertSignId = getIntent().getIntExtra(AppConstant.INTENT_EXPERT_SIGN_ID, 0);
        mName = getIntent().getStringExtra(AppConstant.INTENT_NAME);
        //追加视频的额外参数
        mIsAppendVideo = getIntent().getBooleanExtra(AppConstant.INTENT_IS_APPEND_VIDEO, false);
        mUserSignId = getIntent().getIntExtra(AppConstant.INTENT_USER_SIGN_ID, 0);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_sign_pay;
    }

    @Override
    protected void initTitle() {
        TextView titleView = (TextView) findViewById(R.id.tv_left_title);
        titleView.setText("支付");
        findViewById(R.id.iv_right).setVisibility(View.GONE);
        setOnClickListener(findViewById(R.id.iv_left));
    }

    @Override
    protected void initContent() {
        mContentView = findViewById(R.id.ll_content);
        mContentView.setVisibility(View.GONE);
        mNameView = (EditText) findViewById(R.id.et_name);
        mRequirementEt = (EditText) findViewById(R.id.et_requirement);
        mHeaderView = (SimpleDraweeView) findViewById(R.id.sdv_sign);
        mProductNameView = (TextView) findViewById(R.id.tv_product_name);
        mTermView = (TextView) findViewById(R.id.tv_product_owner);
        mPriceView = (TextView) findViewById(R.id.tv_price);
        mDelayTimeDescView = (TextView) findViewById(R.id.tv_delay_time_desc);

        mPayView = findViewById(R.id.tv_pay);

        mIsVideoCheckBox = (CheckBox) findViewById(R.id.img_video_indicator);

        mResultPriceTv = (TextView) findViewById(R.id.tv_result_price);
        mPreferentialPriceTv = (TextView) findViewById(R.id.tv_preferential_price);

        setOnClickListener(mPayView);

        mIsVideoCheckBox.setChecked(true);
        mIsVideoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mProductType = isChecked ? "2" : "1";//追加视频时，cb不可调,所以这里不用考虑
                //请求价格
                NetUtils.getInstance().postNocache(mContext, NetConstant.URL.GET_EXPERT_SIGN_PRICE.getUrl(mContext),
                        NetConstant.getExpertSignPriceParams(mContext, mExpertSignId, 0, mProductType), new OnExpertSignPriceCallback((Activity) mContext));
            }
        });

        mVideoPriceTv = (TextView) findViewById(R.id.tv_video_price);
        mSelectEnNameEntry = findViewById(R.id.tv_red_select_english_name);

        setOnClickListener(mSelectEnNameEntry);
        initLoadStateView();
    }

    /**
     * 获取价格信息回调
     */
    private class OnExpertSignPriceCallback extends NetUtils.Callback<PriceResponse> {

        public OnExpertSignPriceCallback(Activity activity) {
            super(activity, PriceResponse.class);
        }

        @Override
        public void onNetSuccess(PriceResponse response) {
            //价格显示
            if (response == null) {
                DialogUtils.showShortPromptToast(mContext, R.string.tip_get_price_failed);
                return;
            }
            double price = response.getResult().getPrice();//拿到优惠之前的价格
            boolean isChecked = mIsVideoCheckBox.isChecked();
            mFinalPrice = (int) price;
            //合计价格 ui操作
            String priceStr = StringUtils.getString(StringUtils.getPrice(mFinalPrice), "￥");
            mResultPriceTv.setText(priceStr);
            //包含视频时的优惠价格处理
            if (isChecked && mPreferential != 0) {
                mPreferentialPriceTv.setVisibility(View.VISIBLE);
            } else {
                mPreferentialPriceTv.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onNetFailed(HttpException e) {
            DialogUtils.showShortPromptToast(mContext, e.getLocalizedMessage());
        }
    }


    /**
     * 初始化加载状态View, 包括加载中和加载失败
     */
    protected void initLoadStateView() {
        mLoadStateContainer = findViewById(R.id.load_state_container);
        mLoadFailedContgainer = mLoadStateContainer.findViewById(R.id.ll_load_failed_container);
        mRetryBtn = (Button) mLoadStateContainer.findViewById(R.id.btn_retry);
        mRetryBtn.setOnClickListener(this);//重试点击事件处理
        mLoadingProgressBar = (CircleProgressBar) mLoadStateContainer.findViewById(R.id.loading_progressbar);
        mLoadingProgressBar.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light,
                android.R.color.holo_green_light, android.R.color.holo_red_light);
        mLoadingProgressBar.setVisibility(View.GONE);
    }

    /**
     * 显示pb
     */
    protected void showLoadingProgressBar() {
        mContentView.setVisibility(View.GONE);
        mLoadFailedContgainer.setVisibility(View.GONE);
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 显示加载失败的UI
     */
    protected void showLoadFailedUi(String error) {
        mContentView.setVisibility(View.GONE);
        mLoadFailedContgainer.setVisibility(View.VISIBLE);
        mLoadingProgressBar.setVisibility(View.GONE);
    }

    /**
     * 显示rcv
     */
    protected void showContentView() {
        mContentView.setVisibility(View.VISIBLE);
        mLoadFailedContgainer.setVisibility(View.GONE);
        mLoadingProgressBar.setVisibility(View.GONE);
    }


    @Override
    protected void attachData() {
        if (mExpertSignId == AppConstant.SIGN_TYPE_ENGLISH || mExpertSignId == AppConstant.SIGN_TYPE_ENGLISH_VIDEO) {
            mNameView.setHint(R.string.input_english_name);//英文名
            mSelectEnNameEntry.setVisibility(View.VISIBLE);
        } else {
            mNameView.setHint(R.string.input_name);
            mSelectEnNameEntry.setVisibility(View.GONE);
        }
        mNameView.setText(mName);

        if (mIsAppendVideo) {//如果是追加视频，英文名不能选
            mSelectEnNameEntry.setVisibility(View.GONE);
        }

        //如果是追加视频,则只显示视频教程及价格选项,且视频为必选项，不可调
        findViewById(R.id.tv_design_option_tip).setVisibility(mIsAppendVideo ? View.GONE : View.VISIBLE);
        findViewById(R.id.cb_design).setVisibility(mIsAppendVideo ? View.GONE : View.VISIBLE);
        mIsVideoCheckBox.setEnabled(!mIsAppendVideo);

        showLoadingProgressBar();
        // 请求订单数据
        mHandlerList.add(NetUtils.getInstance().postNocache(mContext, NetConstant.URL.SIGN_DETAIL_URL.getUrl(mContext),
                NetConstant.getSignDetailParams(mContext, mExpertSignId), new SignDetailListener(mActivity)));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_pay) {
            if (!mIsPayActioned) {
                HashMap<String, String> map = new HashMap<>();
                pay();
            }
            return;
        }

        if (id == R.id.iv_left) {
            finish();
            return;
        }

        if (id == R.id.tv_red_select_english_name) {
            jump(mContext, EnglishNameListActivity.class, REQUEST_CODE_SELECT_SELECT_EN_NAME);
            return;
        }

        if (id == R.id.btn_retry) {
            showLoadingProgressBar();
            // 请求订单数据
            mHandlerList.add(NetUtils.getInstance().postNocache(mContext, NetConstant.URL.SIGN_DETAIL_URL.getUrl(mContext),
                    NetConstant.getSignDetailParams(mContext, mExpertSignId), new SignDetailListener(mActivity)));
            return;
        }

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", mProductNameView.getText().toString());
        map.put("method", mPayMethod);
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                pay();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    }

    /**
     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
     * 最终支付成功根据异步通知为准
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_LOGIN:
                    pay();
                    break;
                case REQUEST_CODE_PAYMENT:
                    String result = data.getExtras().getString("pay_result");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("name", mProductNameView.getText().toString());
                    map.put("method", mPayMethod);
                    if (!TextUtils.isEmpty(result)) {
                        switch (result) {
                            case AppConstant.Pay_Success:
                                finish();
                                break;
                            case AppConstant.Pay_Cancel:
                                mIsPayActioned = false;
                                DialogUtils.showConfirmDialog(mContext, R.string.pay_cancel, this);
                                break;
                            case AppConstant.Pay_Fail:
                                mIsPayActioned = false;
                                DialogUtils.showConfirmDialog(mContext, R.string.pay_failed, this);
                                break;
                            case AppConstant.Pay_Invalid:
                                mIsPayActioned = false;
                                DialogUtils.showConfirmDialog(mContext, R.string.pay_failed, this);
                                break;
                            default:
                                mIsPayActioned = false;
                                DialogUtils.showConfirmDialog(mContext, R.string.pay_failed, this);
                                break;
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_SELECT_EN_NAME:
                    if (data != null && !TextUtils.isEmpty(data.getStringExtra(AppConstant.INTENT_SELECTED_EN_NAME))) {
                        mNameView.setText(data.getStringExtra(AppConstant.INTENT_SELECTED_EN_NAME));
                    }
                    break;
            }
        }
    }

    private void pay() {
        mName = mNameView.getText().toString().trim();

        if (TextUtils.isEmpty(mName)) {
            DialogUtils.showShortPromptToast(mContext, R.string.input_name);
            return;
        }

        if (!StringUtils.checkStringFormat(mName, mNameFormatReg)) {
            DialogUtils.showShortPromptToast(mContext, mNameFormatDesc);
            return;
        }

        if (mExpertSignId == AppConstant.SIGN_TYPE_ENGLISH && !StringUtils.isValidEnglish(mName)) {
            DialogUtils.showShortPromptToast(mContext, R.string.input_english_name);
            return;
        }

        mRequirement = "";
        if (!TextUtils.isEmpty(mRequirementEt.getText().toString().trim())) {
            mRequirement = mRequirementEt.getText().toString().trim();
        }

        if (TextUtils.isEmpty(mPayMethod)) {
            DialogUtils.showShortPromptToast(mContext, "请至少选择一种支付方式");
        } else if (TextUtils.isEmpty(mName)) {
            DialogUtils.showShortPromptToast(mContext, "名字不能为空哦");
        } else if (!UserInfoUtils.checkUserLogin(mContext)) {
            jump(mContext, UserLogInActivity.class, REQUEST_CODE_LOGIN);
        } else {
            // 获取charge对象
            if (NetUtils.isNetworkConnected(mContext)) {
                String url;
                RequestParams chargeParams;
                if (!mIsAppendVideo) {
                    url = NetConstant.URL.PAY_CHANNEL_CHARGE_URL.getUrl(mContext);
                    chargeParams = NetConstant.getChargeParams(mContext, mPayMethod, mExpertSignId, mName, mRequirement, mProductType, 0);
                } else {
                    //追加视频
                    url = NetConstant.URL.APPEND_VIDEO_URL.getUrl(mContext);
                    chargeParams = NetConstant.getVideoChargeParams(mContext, mPayMethod, mRequirement, mUserSignId);
                }
                mIsPayActioned = true;
                mHandlerList.add(NetUtils.getInstance().postNocache(mContext, url, chargeParams, new ChargeListener(mActivity)));
            } else {
                mIsPayActioned = false;
            }
        }
    }

    /**
     * 签名详情回调
     */
    private class SignDetailListener extends NetUtils.Callback<SignDetailResponse> {
        public SignDetailListener(Activity activity) {
            super(activity, SignDetailResponse.class);
        }

        @Override
        public void onNetSuccess(SignDetailResponse response) {
            showContentView();
            SignDetailResponse.ResultEntity sign = response.getResult();
            // 预览图
            mHeaderView.setImageURI(Uri.parse(sign.getImage_url()));
            // 产品名称
            mProductNameView.setText(sign.getSign_name());

            mNameFormatReg = sign.getRegular();//正则表达式格式
            mNameFormatDesc = sign.getRegular_desc();
            // 价格
            //设计稿价格
            mDesignPrice = sign.getDesign_price();
            mVideoPrice = sign.getVideo_price();
            mPreferential = sign.getPreferential();
            if (mPreferential == 0 || mIsAppendVideo) {//无优惠价格 或者是追加视频,不显示优惠价格
                mPreferentialPriceTv.setVisibility(View.GONE);
            } else {
                mPreferentialPriceTv.setVisibility(View.VISIBLE);
                mPreferentialPriceTv.setText(StringUtils.getString("(优惠", StringUtils.getPrice(mPreferential), "￥)"));
            }
            mTotalPrice = mDesignPrice + mVideoPrice - mPreferential;//合计价格

            if (!mIsAppendVideo) {
                //有视频为合计价格,无视频为手稿价格
                mFinalPrice = mIsVideoCheckBox.isChecked() ? mTotalPrice : mDesignPrice;//默认勾选的合计价格,根据CB状态刷新合计价格
            } else {
                mFinalPrice = mVideoPrice;//追加视频只有视频价格
            }

            String price = StringUtils.getString(StringUtils.getPrice(mDesignPrice), "￥");
            String finalPrice = StringUtils.getString(StringUtils.getPrice(mFinalPrice), "￥");
            String videoPrice = StringUtils.getString("(视频", StringUtils.getPrice(mVideoPrice), ")￥");
            mVideoPriceTv.setText(videoPrice);
            if (!mIsAppendVideo) {
                mPriceView.setText(price);//产品价格
                mNameView.setEnabled(true);
                mNameView.setBackgroundResource(R.drawable.bg_content_stroke);
            } else {
                mPriceView.setText("已设计");
                mNameView.setEnabled(false);
                mNameView.setBackgroundColor(getResources().getColor(R.color.transparent));
            }

            mResultPriceTv.setText(finalPrice);//合计价格
            // 团队
            mTermView.setText(sign.getDesigner_identity());
            // 延时完成
            SimpleDateFormat format = new SimpleDateFormat("MM月dd日HH:mm", Locale.CHINA);
            String deadline = format.format(new Date(sign.getDeadline_time_stamp() * 1000L));
            mDelayTimeDescView.setText(getString(R.string.complete_before_deadline, deadline));
        }

        @Override
        public void onNetFailed(HttpException e) {
            showLoadFailedUi(e.getLocalizedMessage());
        }
    }

    /**
     * 获取charge对象回调
     */
    private class ChargeListener extends NetUtils.Callback<ChargeResponse> {
        public ChargeListener(Activity activity) {
            super(activity, ChargeResponse.class);
        }

        @Override
        public void onNetSuccess(ChargeResponse response) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            ComponentName componentName = new ComponentName(packageName, "com.qicode.mylibrary.activity.wxapi.WXEntryActivity");
            intent.setComponent(componentName);
            Gson gson = new Gson();
            intent.putExtra(PaymentActivity.EXTRA_CHARGE, gson.toJson(response.getResult().getCharge()));
            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        }

        @Override
        public void onNetFailed(HttpException e) {
            super.onNetFailed(e);
            mIsPayActioned = false;
        }
    }
}
