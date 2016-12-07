package com.qicode.mylibrary.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by StarShine on 14-10-11.
 */
public class MD5Utils {
    public static String getMD5(String val) {
        if (val != null && !val.isEmpty()) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(val.getBytes());
                byte[] m = md5.digest();
                return getString(m);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return val;

    }

    /**
     * 拼接字符串并MD5
     *
     * @param objs
     * @return
     */
    public static String getMd5(Object... objs) {
        String rawStr = StringUtils.getString(objs);
        return getMD5(rawStr);
    }

    private static String getString(byte[] hash) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                sb.append("0");
            sb.append(Integer.toHexString(b & 0xFF));
        }
        return sb.toString();
    }
}
