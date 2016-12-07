package com.qicode.mylibrary.constant;

import android.content.Context;

import com.lidroid.xutils.http.RequestParams;
import com.qicode.mylibrary.JniUtil;
import com.qicode.mylibrary.util.AppUtil;
import com.qicode.mylibrary.util.DeviceUtils;
import com.qicode.mylibrary.util.MD5Utils;
import com.qicode.mylibrary.util.StringUtils;
import com.qicode.mylibrary.util.UserInfoUtils;

/**
 * Created by kakaxicm on 2015/9/3.
 * <p>
 * the manager of url and params
 */
public class NetConstant {
    public static final String KEY_CACHE_NAME_MD5 = "cache_md5";
    public static final int DEFAULT_PAGE_COUNT = 100;
    // official website of gift
    private static final String GiftHost = "http://gift.qicodetech.com/";
    //    private static final String GiftHost = "http://gift.abelhu.com/";
    public static final String GET_GIFT_APP_STATUS_URL = GiftHost + "app/get_latest_version/";

    // official website of sign
    private static final String WWWHost = "http://www.artsignpro.com/";
    //    private static final String WWWHost = "http://www.test.artsignpro.com/";
//    private static final String WWWHost = "http://192.168.1.115:10000/";
    public static final String WWW_WEB = WWWHost + "web/free_sign/";

    public static final String WWW_DESIGNER = WWWHost + "web/designer/";

    public static final String WWW_QUESTIONS = WWWHost + "web/question/";

    // the sign host in different environment
    private static final String LocalUrl = "http://192.168.1.103:10000/";
    private static final String TestUrl = "http://sign.test.qima.tech/";
    private static final String ProductionUrl = "http://sign.qima.tech/";
    private static final String PreviewUrl = "http://sign.preview.qima.tech/";

    // the main host of sign
    private static String mHost;

    public enum HostType {
        Local, Test, Production, Preview
    }

    public enum URL {
        USER_LOGIN_URL("comm/login/"),
        PAY_CHANNEL_LIST_URL("pay/channel_list/"),
        PAY_CHANNEL_CHARGE_URL("pay/create_charge/"),
        SIGN_LIST_URL("sign/sign_list/"),
        SIGN_DETAIL_URL("sign/expert_sign_details/"),
        SEND_VERIFY_CODE_URL("comm/send_verify_code/"),
        GET_FONT_LIST_URL("font/get_font_list/"),
        USER_SIGN_COMMENT("sign/add_user_sign_comment/"),
        APPEND_VIDEO_URL("pay/create_addition_video_charge/"),

        GET_CUSTOM_SIGN_INTRO("customized_sign/get_introductions/"),
        GET_CUSTOM_SIGN_LIST("customized_sign/get_customized_sign_list/"),
        GET_CUSTOM_SIGN_OPTIONS_URL("customized_sign/get_options/"),
        GET_CUSTOM_PRICE_URL("customized_sign/get_price/"),
        GET_CUSTOM_CHARGE_URL("customized_sign/create_charge/"),
        GET_CUSTOM_SIGN_DETAIL("customized_sign/get_customized_sign_detail/"),
        GET_CUSTOM_SIGN_SUGGESTIONS_URL("customized_sign/add_modify_suggestion/"),
        GET_CUSTOM_SIGN_RECORD_URL("customized_sign/add_script_record/"),
        GET_EXPERT_SIGN_PRICE("pay/get_price/"),//获取专家价格
        GET_USER_COUPON_LIST("coupon/get_user_coupon/"),//获取优惠券列表
        GET_COUPON_INTRODUCE("coupon/get_coupon_desc/"),//获取优惠券说明
        GET_CREATE_COUPON("coupon/get_coupon/"),//创建优惠券
        HOST("");

        private String url;

        URL(String url) {
            this.url = url;
        }

        public String getUrl(Context context) {
            return StringUtils.getString(getHost(context), url);
        }
    }

    public static String switchHost(HostType type) {
        switch (type) {
            case Local:
                mHost = LocalUrl;
                break;
            case Test:
                mHost = TestUrl;
                break;
            case Production:
                mHost = ProductionUrl;
                break;
            case Preview:
                mHost = PreviewUrl;
        }
        return mHost;
    }

    private static String getHost(Context context) {
        if (StringUtils.isNullOrEmpty(mHost)) {
            if (DeviceUtils.isApkDebug(context)) {
                switchHost(HostType.Test);
            } else {
                switchHost(HostType.Production);
            }
        }
        return mHost;
    }


    public static RequestParams getRegisterParams(Context context, String userPhone, String code) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("user_phone", userPhone);
        params.addBodyParameter("code", code);
        params.addBodyParameter("is_rl", "1");
        return params;
    }

    public static RequestParams getSignListParams(Context context, int page, int count) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count", String.valueOf(count));
        params.addBodyParameter("video", String.valueOf(2));

        //记录缓存的文件名称
        String cacheMd5 = MD5Utils.getMd5("page", page, "count", count, "video", 2);
        params.addQueryStringParameter(KEY_CACHE_NAME_MD5, cacheMd5);
        return params;
    }

    public static RequestParams getSignDetailParams(Context context, int id) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("expert_sign_id", String.valueOf(id));

        //记录缓存的文件名称
        String cacheMd5 = MD5Utils.getMd5("expert_sign_id", String.valueOf(id));
        params.addQueryStringParameter(KEY_CACHE_NAME_MD5, cacheMd5);
        return params;
    }

    public static RequestParams getChargeParams(Context context, String channel, int productId, String name, String requirement, String productType, int couponId) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("channel", channel);
        params.addBodyParameter("expert_sign_id", String.valueOf(productId));
        params.addBodyParameter("sign_user_name", name);
        params.addBodyParameter("addition_content", requirement);
        params.addBodyParameter("sign_type", productType);
        params.addBodyParameter("coupon_id", String.valueOf(couponId));
        return params;
    }

    /**
     * 追加视频的请求参数
     */
    public static RequestParams getVideoChargeParams(Context context, String channel, String requirement, int userSignId) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("channel", channel);
        params.addBodyParameter("addition_video_content", requirement);//追加视频的附加内容
        params.addBodyParameter("user_sign_id", String.valueOf(userSignId));
        return params;
    }

    /**
     * 获取免费字体列表
     */
    public static RequestParams getFreeFontListParams(Context context, String content) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("content", content);
        //记录缓存的文件名称
        String cacheMd5 = MD5Utils.getMd5("content", content);
        params.addQueryStringParameter(KEY_CACHE_NAME_MD5, cacheMd5);
        return params;
    }

    /**
     * 用户签名添加评论
     */
    public static RequestParams getUserSignComment(Context context, int userSignId, String content, String starLevel) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("user_sign_id", userSignId + "");
        params.addBodyParameter("content", content);
        params.addBodyParameter("star_level", starLevel);
        return params;
    }

    /**
     * 发送验证码请求
     *
     * @param tel  验证号码
     * @param type 1.短信验证 2.语音验证
     */
    public static RequestParams getSendAuthCodeParams(Context context, String tel, String type) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("user_phone", tel);
        params.addBodyParameter("verify_type", type);
        return params;
    }


    /**
     * 获取基本公用的参数
     */
    private static RequestParams getBaseParams(Context context) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("identify", context.getPackageName());
        params.addBodyParameter("platform", "android");
        params.addBodyParameter("app_version", AppUtil.getAppVersionCode(context));
        params.addBodyParameter("phone_model", DeviceUtils.getModel());
        params.addBodyParameter("phone_version", DeviceUtils.getOSVersion());
        String timeStamp = String.valueOf(System.currentTimeMillis());
        params.addBodyParameter("timestamp", timeStamp);

        int userId = UserInfoUtils.getUserId(context);
        String userIdStr = "";
        if (userId != 0) {
            userIdStr = String.valueOf(userId);
        }
//        params.addBodyParameter("user_id", userIdStr);
        //TODO revert
        params.addBodyParameter("user_id", userIdStr);
//        params.addBodyParameter("market", AppUtil.getUMengChannel(context));
        params.addBodyParameter("market", "tencent");

//        params.addBodyParameter("business_key", "key_c219bbde461c49c52f847e19149d784d");

        params.addBodyParameter("business_key", UserInfoUtils.getBusinessKey(context));

        // add signature
//        JniUtil jniUtil = new JniUtil();
//        String signature = StringUtils.toMD5(StringUtils.getString(userIdStr, "secret_a6a05183fc1e92f0a5fdb13f0a14cef5", timeStamp));
        String signature = StringUtils.toMD5(StringUtils.getString(userIdStr, UserInfoUtils.getBusinessSecret(context), timeStamp));
        params.addBodyParameter("signature", signature);

        return params;
    }

    /**
     * 定制签名介绍请求参数
     */
    public static RequestParams getCustomSignIntroParams(Context context) {
        return getBaseParams(context);
    }

    /**
     * 用户定制签名列表参数
     */
    public static RequestParams getCustomSignListParams(Context context) {
        RequestParams params = getBaseParams(context);
        //记录缓存的文件名称
        String cacheMd5 = MD5Utils.getMd5("user_id", UserInfoUtils.getUserId(context));
        params.addQueryStringParameter(KEY_CACHE_NAME_MD5, cacheMd5);
        return getBaseParams(context);
    }

    /**
     * 获取订制签名可选项参数
     */
    public static RequestParams getCustomOptionsParams(Context context) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("version", "0");
        return params;
    }

    /**
     * @param scriptNum   满意手稿个数
     * @param modifyNum   修改次数
     * @param videoNum    视频个数
     * @param designerId  设计师ID
     * @param payMethodId 支付方式
     */
    public static RequestParams getCustomPriceParams(Context context, int scriptNum, int modifyNum, int videoNum, int designerId, int payMethodId, int couponId) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("script_count", String.valueOf(scriptNum));
        params.addBodyParameter("satisfied_video_count", String.valueOf(videoNum));
        params.addBodyParameter("modify_times_count", String.valueOf(modifyNum));
        params.addBodyParameter("designer_id", String.valueOf(designerId));
        params.addBodyParameter("pay_method_id", String.valueOf(payMethodId));
        params.addBodyParameter("coupon_id", String.valueOf(couponId));
        return params;
    }

    /**
     * 生成Charge对象
     */
    public static RequestParams getCustomSignChargeParams(Context context,
                                                          String name,
                                                          String addition,
                                                          int script_count,
                                                          int satisfied_video_count,
                                                          int modify_times_count,
                                                          int designer_id,
                                                          int pay_method_id,
                                                          int coupon_id) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("name", name);
        params.addBodyParameter("addition", addition);
        params.addBodyParameter("script_count", String.valueOf(script_count));
        params.addBodyParameter("satisfied_video_count", String.valueOf(satisfied_video_count));
        params.addBodyParameter("modify_times_count", String.valueOf(modify_times_count));
        params.addBodyParameter("designer_id", String.valueOf(designer_id));
        params.addBodyParameter("pay_method_id", String.valueOf(pay_method_id));
        params.addBodyParameter("coupon_id", String.valueOf(coupon_id));
        return params;
    }

    public static RequestParams getCustomSignDetail(Context context, long orderId) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("id", String.valueOf(orderId));
        return params;
    }

    public static RequestParams getCommitCustomSuggestionsPrams(Context context, long orderId, String suggestion) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("charge_id", String.valueOf(orderId));
        params.addBodyParameter("suggestions", suggestion);
        return params;
    }

    public static RequestParams getCommitCustomRecordPrams(Context context, long orderId, String scripts) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("charge_id", String.valueOf(orderId));
        params.addBodyParameter("scripts", scripts);
        return params;
    }

//    /**
//     * 获取礼物app状态参数
//     */
//    public static RequestParams getGiftAppStatusParams() {
//        RequestParams params = getGiftBaseParams();
//        params.addBodyParameter("identify", "com.qicode.giftbox");
//        params.addBodyParameter("platform", "android");
//        return params;
//    }

//    /**
//     * 获取礼物基本公用的参数
//     */
//    private static RequestParams getGiftBaseParams() {
//        RequestParams params = new RequestParams();
//        //timeStamp
//        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
//        params.addBodyParameter("timestamp", timeStamp);
//        //signature
//        JniUtil jniUtil = new JniUtil();
//        String signature = StringUtils.toMD5(StringUtils.getString(timeStamp, jniUtil.getGiftSecret()));
//        params.addBodyParameter("signature", signature);
//        return params;
//    }

    /**
     * 获取专家签名价格
     *
     * @param context
     * @param expertSignId 专家签名id
     * @param couponId     优惠券id
     * @param productType  签名类型 1:只有设计图;2:设计图和视频都有;3:追加视频
     * @return
     */
    public static RequestParams getExpertSignPriceParams(Context context, int expertSignId, int couponId, String productType) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("coupon_id", StringUtils.getString(couponId));
        params.addBodyParameter("expert_sign_id", StringUtils.getString(expertSignId));
        params.addBodyParameter("sign_type", productType);
        return params;
    }

    /**
     * 获取优惠券列表
     *
     * @param context
     * @return
     */
    public static RequestParams getUserCouponListParams(Context context) {
        RequestParams params = getBaseParams(context);
        return params;
    }

    /**
     * 获取优惠券说明
     * @param context
     * @return
     */
    public static RequestParams getCouponInfoParams(Context context) {
        RequestParams params = getBaseParams(context);
        return params;
    }

    /**
     * 创建优惠券
     * @param context
     * @param typeSymbol 优惠券创建来源,由后台确定
     * @return
     */
    public static RequestParams getCreateCouponParams(Context context, AppConstant.COUPON_TYPE_SYMBOL typeSymbol) {
        RequestParams params = getBaseParams(context);
        params.addBodyParameter("type_symbol", typeSymbol.getSymbol());
        return params;
    }
}
