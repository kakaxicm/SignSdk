
package com.qicode.mylibrary.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;

public class DeviceUtils {

    /**
     * 获取手机的基本信息
     */
    public static String getDeviceBaseInfo() {
        //BOARD 主板  
        String phoneInfo = "BOARD: " + android.os.Build.BOARD;
        phoneInfo += "\n BOOTLOADER: " + android.os.Build.BOOTLOADER;
        //BRAND 运营商  
        phoneInfo += "\n BRAND: " + android.os.Build.BRAND;
        //DEVICE 驱动  
        phoneInfo += "\n DEVICE: " + android.os.Build.DEVICE;
        //DISPLAY 显示  
        phoneInfo += "\n DISPLAY: " + android.os.Build.DISPLAY;
        //指纹  
        phoneInfo += "\n FINGERPRINT: " + android.os.Build.FINGERPRINT;
        //HARDWARE 硬件  
        phoneInfo += "\n HARDWARE: " + android.os.Build.HARDWARE;
        phoneInfo += "\n HOST: " + android.os.Build.HOST;
        phoneInfo += "\n ID: " + android.os.Build.ID;
        //MANUFACTURER 生产厂家  
        phoneInfo += "\n MANUFACTURER: " + android.os.Build.MANUFACTURER;
        //MODEL 机型  
        phoneInfo += "\n MODEL: " + android.os.Build.MODEL;
        phoneInfo += "\n PRODUCT: " + android.os.Build.PRODUCT;
        phoneInfo += "\n RADITAGSO: " + android.os.Build.TAGS;
        phoneInfo += "\n TIME: " + android.os.Build.TIME;
        phoneInfo += "\n TYPE: " + android.os.Build.TYPE;
        phoneInfo += "\n USER: " + android.os.Build.USER;
        //VERSION.RELEASE 固件版本  
        phoneInfo += "\n VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
        phoneInfo += "\n VERSION.CODENAME: " + android.os.Build.VERSION.CODENAME;
        //VERSION.INCREMENTAL 基带版本  
        phoneInfo += "\n VERSION.INCREMENTAL: " + android.os.Build.VERSION.INCREMENTAL;
        //VERSION.SDK SDK版本  
        phoneInfo += "\n VERSION.SDK_INT: " + android.os.Build.VERSION.SDK_INT;
        return phoneInfo;
    }

    /**
     * 获取网络连接状态
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取网络连接类型
     */
    public static int getNetworkConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 获取手机目前使用的语言
     */
    public static String getLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        return locale.getLanguage();
    }

    public static String getNation(Context context) {
        TelephonyManager tn = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tn.getSimCountryIso();
    }

    /**
     * 获取手机屏幕密度
     */
    public static float getDensity(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        return dm.density;
    }

    /**
     * 判断是否为平板
     */
    public static boolean isPad(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        // 大于6尺寸则为Pad
        return screenInches >= 6.0;
    }

    /**
     * 判断手机是否处于debug模式
     */
    public static boolean isApkDebug(Context context) {
        try {
            return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取手机ip
     */
    public static String getLocalIpAddress() {
        String ipAddress = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddress = networkInterface.getInetAddresses(); enumIpAddress.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddress.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ipAddress = ipAddress + ";" + inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return ipAddress;
    }

    /**
     * 友盟
     */
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (StringUtils.isNullOrEmpty(device_id)) {
                device_id = mac;
            }

            if (StringUtils.isNullOrEmpty(device_id)) {
                device_id = android.provider.Settings.Secure
                        .getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getModel() {
        return android.os.Build.MODEL;
    }

    public static String getOSVersion(){
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 检测SD卡
     */
    public static boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
}