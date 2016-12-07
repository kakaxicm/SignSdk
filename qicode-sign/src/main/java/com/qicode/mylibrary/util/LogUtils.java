
package com.qicode.mylibrary.util;

import android.content.Context;
import android.util.Log;

/**
 * Created by huyongsheng on 2014/7/18.
 */
public class LogUtils {

    public static void d(Context context, String tag, Object... msg) {
        if (DeviceUtils.isApkDebug(context)) {
            Log.d(tag, StringUtils.getString(msg));
        }
    }

    public static void e(Context context, String tag, Object... msg) {
        if (context != null) {
            if (DeviceUtils.isApkDebug(context)) {
                Log.e(tag, StringUtils.getString(msg));
            }
        } else {
            Log.e(tag, StringUtils.getString(msg));
        }
    }

    public static void i(Context context, String tag, Object... msg) {
        if (DeviceUtils.isApkDebug(context)) {
            Log.i(tag, StringUtils.getString(msg));
        }
    }

    public static void showTime(Context context, Object... msg) {
        if (DeviceUtils.isApkDebug(context)) {
            Log.e("System Time:", StringUtils.getString(System.currentTimeMillis(), StringUtils.getString(msg)));
        }
    }
}
