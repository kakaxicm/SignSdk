package com.qicode.mylibrary.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by kakaxicm on 2015/10/24.
 */
public class AppUtil {
    public static String getAppVersionCode(Context context) {
        String versionCode = "";
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            if (info != null) {
                versionCode = String.valueOf(info.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getUMengChannel(Context context) {
        String msg = "";
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return msg;
        }
        msg = appInfo.metaData.getString("UMENG_CHANNEL");
        return msg;
    }
}
