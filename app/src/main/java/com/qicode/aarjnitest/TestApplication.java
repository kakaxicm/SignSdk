package com.qicode.aarjnitest;

import android.app.Application;

import com.qicode.mylibrary.QiCodeApi;

/**
 * Created by chenming on 16/11/17.
 */

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        QiCodeApi.init(this, "key_********", "secret_*****");//test
    }
}
