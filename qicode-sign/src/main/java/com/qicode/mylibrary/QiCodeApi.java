package com.qicode.mylibrary;

import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.qicode.mylibrary.activity.ExpertSignListActivity;
import com.qicode.mylibrary.util.UserInfoUtils;

/**
 * Created by chenming on 16/11/12.
 */

public class QiCodeApi {

    public static void invokeSignPage(Context context){
        Intent intent = new Intent(context, ExpertSignListActivity.class);
        context.startActivity(intent);
    }

    public static void init(Context context, String key, String secret){
        Fresco.initialize(context);
        UserInfoUtils.saveBussinessKey(context, key);
        UserInfoUtils.saveBussinessSecret(context, secret);
    }

}
