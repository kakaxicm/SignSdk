package com.qicode.mylibrary;

/**
 * Created by chenming on 16/11/12.
 */

public class JniUtil {
    static {
        System.loadLibrary("native-lib");
    }

    public native String getSecret();
}
