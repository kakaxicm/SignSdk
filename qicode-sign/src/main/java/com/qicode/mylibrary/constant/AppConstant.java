package com.qicode.mylibrary.constant;

public class AppConstant {

    // 填写从短信SDK应用后台注册得到的APPKEY
    public static String SMS_APP_KEY = "9e9ca93f271e";
    // 填写从短信SDK应用后台注册得到的APP_SECRET
    public static String SMS_APP_SECRET = "c0228cd5b79d42ed9f284311e616ee7b";

    // 获取短信通知
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    // 交易渠道
    public static final String CHANNEL_WECHAT = "wx";
    public static final String CHANNEL_ALIPAY = "alipay";
    public static final String CHANNEL_SMS = "sms";
    public static final String CHANNEL_QQ = "qq";

    //支付方式id
    public static final int PAY_METHOD_ID_ALI = 1;
    public static final int PAY_METHOD_ID_WX = 2;

    // 显示或者隐藏浮动图标
    public static final String INTENT_FLOAT_VIEW_ACTION = "float_view_action";
    public static final String INTENT_HIDE_FLOAT_VIEW = "hide_float_view";
    public static final String INTENT_SHOW_FLOAT_VIEW = "show_float_view";

    // intent相关
    public static final String INTENT_IS_ONLINE_SIGN = "intent_is_online_sign";
    public static final String INTENT_NAME = "intent_name";
    public static final String INTENT_IS_APPEND_VIDEO = "intent_is_append_video";//是否追加视频的标记\
    public static final String INTENT_USER_SIGN_ID = "user_sign_id";
    public static final String INTENT_DESIGN_PRICE = "design_price";
    public static final String INTENT_VIDEO_PRICE = "video_price";
    public static final String INTENT_ACTIVITY = "intent_activity";
    public static final String INTENT_SIGN_CONTENT = "sign_content";
    public static final String INTENT_IMAGE_URL = "font_path";
    public static final String INTENT_SHARE_IMG_NAME = "img_file";
    public static final String INTENT_CUSTOM_SIGN_ORDER_ID = "intent_custom_sign_order_id";
    public static final String IMG_MEDIA_STORE_DESC = "SignImg";
    public static final String IMAGE_MEDIA_PREVIEW_STORE_DESC = "SignImg_Preview";
    public static final String SHARE_IMG_FILE_NAME_PRE = "sharing";
    public static final String INTENT_SHARE_ABS_PATH = "share_img_path";
    public static final String INTENT_SHARE_SIGN_PATH = "art_sign_path";
    public static final String INTENT_HEAD_IMG_PATH = "head_img_path";
    public static final String INTENT_POETRY_TITLE = "poetry_title";
    public static final String INTENT_POETRY_CONTENT = "poetry_content";
    public static final String INTENT_TYPEFACE_REELECT = "is_typeface_reelect";
    public static final String INTENT_EXPERT_SIGN_ID = "INTENT_EXPERT_SIGN_ID";
    public static final String INTENT_VIDEO_URL = "INTENT_VIDEO_URL";
    public static final String INTENT_VIDEO_NAME = "INTENT_VIDEO_NAME";
    public static final String INTENT_CAN_DOWNLOAD_VIDEO = "INTENT_CAN_DOWNLOAD_VIDEO";
    public static final String INTENT_ACTION = "INTENT_ACTION";
    public static final String INTENT_ONLINE_IMAGE_URL = "INTENT_ONLINE_IMAGE_URL";
    public static final String INTENT_PAY_METHOD = "INTENT_PAY_METHOD";
    public static final String INTENT_SELECTED_EN_NAME = "SELECT_EN_NAME";
    public static final String INTENT_FROM_PAY_SUCCESS = "INTENT_FROM_PAY_SUCCESS";

    public static final String INTENT_CUSTOM_ACTION_TYPE = "custom_action_type";
    public static final String INTENT_CUSTOM_SCRIPTS_JSON = "custom_scripts_json";

    public static final String INTENT_PAY_METHOD_INDEX = "INTENT_PAY_METHOD_INDEX";
    public static final String INTENT_PAY_METHOD_LIST_JSON = "INTENT_PAY_METHOD_LIST_JSON";

    public static final String INTENT_WEBVIEW_URL = "INTENT_WEBVIEW_URL";

    public static final String INTENT_COUPON_FILTER = "INTENT_COUPON_FILTER";
    public static final String INTENT_COUPON_RESULT = "INTENT_COUPON_RESULT";
    public static final String INTENT_IS_NEED_OPEN_MAIN_PAGE = "INTENT_IS_NEED_OPEN_MAIN_PAGE";
    public static final String INTENT_MAIN_PAGE_INDEX = "INTENT_MAIN_PAGE_INDEX";

    // 交易结果
    public static final String Pay_Success = "success";
    public static final String Pay_Fail = "fail";
    public static final String Pay_Cancel = "cancel";
    public static final String Pay_Invalid = "invalid";

    // action相关
    public static final String ACTION_PAY = "ACTION_PAY";

    public static final int SIGN_TYPE_ENGLISH = 18;
    public static final int SIGN_TYPE_ENGLISH_VIDEO = 19;

    public final static int ACTION_TYPE_MODIFY = 1;
    public final static int ACTION_TYPE_RECORD = 2;

    // 礼物
    public final static String GIFT_PACKAGE_NAME = "com.qicode.giftbox";

    /**
     * 优惠券适用的消费方式
     */
    public enum COUPON_SPEND_TYPE {
        METHOD_EXPERT_SIGN("expert_sign"),//用于专家签名
        METHOD_CUSTOM_SIGN("custom_sign"),//用于定制签名
        METHOD_UNIVERSAL("all_sign");//通用优惠券;

        private String type;

        COUPON_SPEND_TYPE(String type) {
            this.type = type;
        }

        public String getSpendMethod(){
            return type;
        }

    }

    /**
     * 优惠券的创建类型,由后台确定
     */
    public enum COUPON_TYPE_SYMBOL {
        FREE_SIGN_SHARE("free_sign_share"),//免费签名分享
        USER_PRAISE("user_praise"),//用户好评
        UNIVER_TEST("user_universal_action");//通用优惠券测试

        private String type;

        COUPON_TYPE_SYMBOL(String type) {
            this.type = type;
        }

        public String getSymbol(){
            return type;
        }

    }

}
