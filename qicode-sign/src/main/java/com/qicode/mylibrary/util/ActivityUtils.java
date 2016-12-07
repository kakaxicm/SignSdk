package com.qicode.mylibrary.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.qicode.mylibrary.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityUtils {

    /**
     * 跳转到google play市场
     */
    public static boolean jumpToGooglePlayByPackage(Activity activity, String packageName) {
        return jumpToGooglePlay(activity, StringUtils.getGooglePlayString(activity, packageName));
    }

    /**
     * 跳转到google play市场
     */
    public static boolean jumpToGooglePlay(Activity activity, String url) {
        boolean result;
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

//    public static void jumpToMarket(Activity activity, String packageName) {
//        Uri uri = Uri.parse(StringUtils.getString("market://details?id=", packageName));
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (isIntentAvailable(activity, intent)) {
//            activity.startActivity(intent);
//        } else {
//            DialogUtils.showShortPromptToast(activity, R.string.not_app_market);
//        }
//    }

//    public static void jumpToMarket(Activity activity, String packageName, List<String> markets, String defaultUrl) {
//        Uri uri = Uri.parse(StringUtils.getString("market://details?id=", packageName));
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PackageManager pm = activity.getPackageManager();
//        List<ResolveInfo> infoList = pm.queryIntentActivities(intent, 0);
//        List<String> marketAppNameList = new ArrayList<>();
//        List<String> marketAppClassList = new ArrayList<>();
//        for (ResolveInfo resolveInfo : infoList) {
//            ActivityInfo activityInfo = resolveInfo.activityInfo;
//            String targetPackageName = activityInfo.packageName;
//            String className = activityInfo.name;
//            marketAppNameList.add(targetPackageName);
//            marketAppClassList.add(className);
//        }
//
//        for (String market : markets) {
//            if (marketAppNameList.contains(market)) {
//                int index = marketAppNameList.indexOf(market);
//                ComponentName cn = new ComponentName(marketAppNameList.get(index), marketAppClassList.get(index));
//                intent.setComponent(cn);
//                activity.startActivity(intent);
//                return;
//            }
//        }
//
//        if (!StringUtils.isNullOrEmpty(defaultUrl)) {
//            uri = Uri.parse(defaultUrl);
//            intent = new Intent(Intent.ACTION_VIEW, uri);
//            activity.startActivity(intent);
//        } else {
//            DialogUtils.showShortPromptToast(activity, R.string.app_not_in_ready);
//        }
//    }

    /**
     * 跳转到指定app
     */
    public static boolean jumpToIKeyBoard(Context context, String packageName, String className) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(packageName, className);
        intent.setComponent(componentName);
        intent.setAction("android.intent.action.MAIN");
        intent.putExtra("source package", "flip font");
        if (isIntentAvailable(context, intent)) {
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 跳转到activity
     */
    public static void jump(Context context, Intent intent) {
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.pull_right_out);
    }

    /**
     * 跳转到activity
     */
    public static void jump(Context context, Class<?> targetClass) {
        Intent intent = new Intent();
        intent.setClass(context, targetClass);
        jump(context, intent);
    }

    /**
     * 跳转到activity
     */
    public static void jump(Activity activity, Class<?> targetClass, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, targetClass);
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.push_left_in, R.anim.pull_right_out);
    }

    /**
     * 跳转到activity
     */
    public static void jump(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 判断intent是否可用
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        if (intent == null) {
            return false;
        } else {
            PackageManager packageManager = context.getPackageManager();
            List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        }
    }

    /**
     * 分享信息
     */
    public static void shareFont(Context content, int subjectRes, String context, int titleRes) {
        shareFont(content, content.getString(subjectRes), context, content.getString(titleRes));
    }

    /**
     * 分享信息
     */
    public static void shareFont(Context content, String subject, String context, CharSequence title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, context);
        content.startActivity(Intent.createChooser(intent, title));
    }

    /**
     * 跳转到相册获取图片
     */
    public static void jumpToAlbum(Activity activity, int requestCode) {
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType("image/*");
        activity.startActivityForResult(getAlbum, requestCode);
    }

    /**
     * 程序是否在前台运行
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(packageName)
                        && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断app是否在运行
     */
    public static boolean isAppRunning(Context context) {
        boolean isAppRunning = false;
        String appName = context.getApplicationContext().getPackageName();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(5);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(appName) || info.baseActivity.getPackageName().equals(appName)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }
}
