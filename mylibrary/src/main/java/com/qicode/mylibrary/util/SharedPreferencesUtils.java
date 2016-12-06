package com.qicode.mylibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.qicode.mylibrary.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huyongsheng on 2014/7/22.
 */
public class SharedPreferencesUtils {

    public static void save(Context context, String shareName, Map<String, Object> map) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value instanceof String) {
                editor.putString(key, (String) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            }
        }
        editor.apply();
    }

    public static void save(Context context, Map<String, Object> map) {
        save(context, context.getString(R.string.app_name), map);
    }

    public static void remove(Context context, String shareName, String... keys) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
    }

    public static Map<String, Object> get(Context context, String shareName, String... keys) {
        Map<String, Object> result = new HashMap<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        Map<String, ?> map = sharedPreferences.getAll();
        for (String key : keys) {
            if (!StringUtils.isNullOrEmpty(key)) {
                result.put(key, map.get(key));
            }
        }
        return result;
    }

    public static Map<String, Object> get(Context context, String... keys) {
        return get(context, context.getString(R.string.app_name), keys);
    }

    public static int getInt(Context context, String key) {
        return getInt(context, context.getString(R.string.app_name), key);
    }

    public static int getInt(Context context, String shareName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    public static void saveInt(Context context, String shareName, String key, int value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        save(context, shareName, map);
    }

    public static void saveInt(Context context, String key, int value) {
        saveInt(context, context.getString(R.string.app_name), key, value);
    }

    public static boolean getBoolean(Context context, String shareName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, context.getString(R.string.app_name), key);
    }

    public static void saveBoolean(Context context, String shareName, String key, boolean value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        save(context, shareName, map);
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        save(context, map);
    }

    public static String getString(Context context, String key) {
        return getString(context, context.getString(R.string.app_name), key);
    }

    public static String getString(Context context, String shareName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void saveString(Context context, String shareName, String key, String value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        save(context, shareName, map);
    }

    public static void saveString(Context context, String key, String value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        save(context, map);
    }

    public static boolean contains(Context context, String key) {
        return contains(context, context.getString(R.string.app_name), key);
    }

    public static boolean contains(Context context, String shareName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }
}
