package com.qicode.mylibrary.util;

/**
 * Created by chenming on 2015/5/22.
 */

import android.content.Context;

import com.qicode.mylibrary.model.UserLoginResponse;


public class UserInfoUtils {
    //鉴宝登陆或者注册成功后的信息
    private static final String SP_KEY_USER_ID = "user_id";
    private static final String SP_KEY_USER_NAME = "user_name";
    private static final String SP_KEY_USER_PHONE = "phone";
    private static final String SP_KEY_BUSINESS_KEY = "business_key";
    private static final String SP_KEY_BUSINESS_SECRET = "business_secret";

    public static void saveUserLoginInfo(Context context, UserLoginResponse.ResultEntity result) {
        if (result != null) {
            SharedPreferencesUtils.saveInt(context, SP_KEY_USER_ID, result.getUser_id());
            SharedPreferencesUtils.saveString(context, SP_KEY_USER_NAME, result.getUser_name());
            SharedPreferencesUtils.saveString(context, SP_KEY_USER_PHONE, result.getUser_phone());
        }
    }

    public static boolean checkUserLogin(Context context) {
        int userId = getUserId(context);
        return userId > 0;
    }

    public static void setName(Context context, String phone) {
        SharedPreferencesUtils.saveString(context, SP_KEY_USER_NAME, phone);
    }

    public static void setPhone(Context context, String phone) {
        SharedPreferencesUtils.saveString(context, SP_KEY_USER_PHONE, phone);
    }

    public static String getPhone(Context context) {
        return SharedPreferencesUtils.getString(context, SP_KEY_USER_PHONE);
    }

    public static int getUserId(Context context) {
        return SharedPreferencesUtils.getInt(context, SP_KEY_USER_ID);
    }

    public static String getUserName(Context context){
        return SharedPreferencesUtils.getString(context, SP_KEY_USER_NAME);
    }

    public static void logOut(Context context) {
        SharedPreferencesUtils.saveInt(context, SP_KEY_USER_ID, 0);
        SharedPreferencesUtils.saveString(context, SP_KEY_USER_NAME, "");
        SharedPreferencesUtils.saveString(context, SP_KEY_USER_PHONE, "");
    }

    public static void saveBussinessKey(Context context, String key){
        SharedPreferencesUtils.saveString(context, SP_KEY_BUSINESS_KEY, key);
    }

    public static void saveBussinessSecret(Context context, String secret){
        SharedPreferencesUtils.saveString(context, SP_KEY_BUSINESS_SECRET, secret);
    }

    public static String getBusinessKey(Context context){
        return SharedPreferencesUtils.getString(context, SP_KEY_BUSINESS_KEY);
    }

    public static String getBusinessSecret(Context context){
        return SharedPreferencesUtils.getString(context, SP_KEY_BUSINESS_SECRET);
    }
}
