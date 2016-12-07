package com.qicode.mylibrary.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.client.HttpRequest;
import com.qicode.mylibrary.R;
import com.qicode.mylibrary.constant.AppConstant;
import com.qicode.mylibrary.constant.NetConstant;
import com.qicode.mylibrary.model.SendAuthCodeResponse;
import com.qicode.mylibrary.model.UserLoginResponse;
import com.qicode.mylibrary.util.DialogUtils;
import com.qicode.mylibrary.util.NetUtils;
import com.qicode.mylibrary.util.StringUtils;
import com.qicode.mylibrary.util.UserInfoUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kakaxicm on 2015/9/3.
 */
public class UserLogInActivity extends BaseActivity {

    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private EditText mPhoneEditView;
    private EditText mAuthCodeEt;
    private TextView mGetAuthCodeBtn;
    private Button mRegisterBtn;
    /**
     * 验证码请求
     */
    private final int AUTH_CODE_TIME_OUT = 60;
    private int mTimeCount = 0;
    private Handler mAuthCodeTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mTimeCount > 0) {
                mGetAuthCodeBtn.setText(StringUtils.getString(mTimeCount, getString(R.string.unit_second)));
                mTimeCount--;
                mAuthCodeTimeHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                mGetAuthCodeBtn.setText(R.string.retry);
                mAuthCodeEt.setText("");
                mAuthCodeTimeHandler.removeMessages(0);
            }
        }
    };

    @Override
    protected void initData() {
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_userlogin;
    }

    @Override
    protected void initTitle() {
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(R.string.register_login);
        View backView = findViewById(R.id.iv_left);
        View userView = findViewById(R.id.iv_right);

        setViewVisible(backView);
        setViewInvisible(userView);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mPhoneEditView = (EditText) findViewById(R.id.et_user_phone);
        mGetAuthCodeBtn = (TextView) findViewById(R.id.get_auth_code);
        mAuthCodeEt = (EditText) findViewById(R.id.et_auth_code);
        mRegisterBtn = (Button) findViewById(R.id.bt_register);
        setOnClickListener(mGetAuthCodeBtn, mRegisterBtn);
    }

    @Override
    protected void attachData() {
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();
        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(AppConstant.SMS_RECEIVED_ACTION);
        intentFilter.setPriority(1000);
        //注册广播
        registerReceiver(mSMSBroadcastReceiver, intentFilter);
    }

    @Override
    protected void refreshData() {
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.iv_left){
            finish();
            return;
        }

        if(id == R.id.get_auth_code){
            sendAuthCode();
            return;
        }

        if(id == R.id.bt_register){
            register();
            return;
        }
    }

    private void register() {
        String tel = mPhoneEditView.getText().toString().trim();
        String authCode = mAuthCodeEt.getText().toString().trim();
        if (TextUtils.isEmpty(tel)) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_empty_number);
        } else if (!StringUtils.checkPhoneNumber(tel)) {
            DialogUtils.showShortPromptToast(mContext, R.string.tip_wrong_number);
        } else if (TextUtils.isEmpty(authCode)) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_cannot_be_empty);
        } else {
            new HttpUtils().send(HttpRequest.HttpMethod.POST, NetConstant.URL.USER_LOGIN_URL.getUrl(mContext), NetConstant.getRegisterParams(mContext,
                    tel, authCode), new UserLoginListener(mActivity));
        }
    }

    private void sendAuthCode() {
        // 校验电话号码有效性
        String tel = mPhoneEditView.getText().toString().trim();
        if (StringUtils.checkPhoneNumber(tel)) {
            if (NetUtils.isNetworkConnected(mContext)) {
                // 验证码获取等待定时器开启
                if (mTimeCount <= 0) {
                    //发送验证码
                    sendGetAuthCodeRequest(tel);
                    //重置定时器
                    mTimeCount = AUTH_CODE_TIME_OUT;
                    mAuthCodeTimeHandler.sendEmptyMessage(0);
                } else {
                    DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_wait);
                }
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.network_not_available);
            }

        } else {
            DialogUtils.showShortPromptToast(mContext, R.string.tip_wrong_number);
        }
    }

    /**
     * 发送验证码请求
     *
     * @param tel 电话
     */
    private void sendGetAuthCodeRequest(String tel) {
        NetUtils.getInstance().postNocache(mContext, NetConstant.URL.SEND_VERIFY_CODE_URL.getUrl(mContext),
                NetConstant.getSendAuthCodeParams(mContext, tel, "1"), new SendAuthCodeCallBack(mActivity));
    }

    private class SendAuthCodeCallBack extends NetUtils.Callback<SendAuthCodeResponse> {


        public SendAuthCodeCallBack(Activity activity) {
            super(activity, SendAuthCodeResponse.class);
        }

        @Override
        public void onNetSuccess(SendAuthCodeResponse response) {
            if (response != null && response.getStatus().getCode().equals("0")) {
                DialogUtils.showShortPromptToast(mContext, R.string.auth_code_send);
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.error_auth_code);
                resetTimer();
            }
        }

        @Override
        public void onNetFailed(HttpException e) {
            super.onNetFailed(e);
            DialogUtils.showShortPromptToast(mContext, R.string.error_auth_code);
            resetTimer();
        }
    }

    private class UserLoginListener extends NetUtils.Callback<UserLoginResponse> {

        public UserLoginListener(Activity activity) {
            super(activity, UserLoginResponse.class);
        }

        @Override
        public void onNetSuccess(UserLoginResponse response) {
            DialogUtils.showShortPromptToast(mContext, R.string.login_success);
            UserInfoUtils.saveUserLoginInfo(mContext, response.getResult());
            //登陆成功, 重新注册信鸽用户
            // 跳转
            setResult(RESULT_OK);
            finish();
        }
    }

    private void resetTimer() {
        mTimeCount = 0;
        mAuthCodeTimeHandler.removeMessages(0);
        mGetAuthCodeBtn.setText(R.string.retry);
        mGetAuthCodeBtn.setEnabled(true);
        setOnClickListener(mGetAuthCodeBtn);
    }

    private class SMSBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppConstant.SMS_RECEIVED_ACTION)) {
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                if (pdus != null && pdus.length > 0) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
                    Pattern pattern = Pattern.compile("\\d{6}");
                    Matcher matcher = pattern.matcher(smsMessage.getDisplayMessageBody());
                    if (matcher.find()) {
                        String s = matcher.group();
                        mAuthCodeEt.setText(s);
                        resetTimer();
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSMSBroadcastReceiver);
    }
}
