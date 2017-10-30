package com.qicode.mylibrary.activity;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qicode.mylibrary.R;
import com.qicode.mylibrary.constant.AppConstant;
import com.qicode.mylibrary.util.BitmapUtils;
import com.qicode.mylibrary.util.DialogUtils;
import com.qicode.mylibrary.util.MD5Utils;

import java.io.FileNotFoundException;

/**
 * Created by kakaxicm on 2015/11/21.
 */
public class SignProductPreviewActivity extends BaseActivity {
    private SimpleDraweeView mPreviewSdv;
    private View mSaveImageBtn;
    private String mUrl;
    private String mSaveFileName;//保存文件名
    private String mSaveFilePath;//保存文件路径
    private Context mContext;

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_img_preview;
    }

    @Override
    protected void getIntentData() {
        mUrl = getIntent().getStringExtra(AppConstant.INTENT_ONLINE_IMAGE_URL);
    }

    @Override
    protected void initContent() {
        mPreviewSdv = (SimpleDraweeView) findViewById(R.id.sdv_preview);
        mPreviewSdv.setAspectRatio(1.33f);
        mPreviewSdv.setImageURI(Uri.parse(mUrl));
        mSaveImageBtn = findViewById(R.id.btn_save_image);
        mSaveImageBtn.setOnClickListener(this);
    }

    @Override
    protected void attachData() {
        mSaveFileName = MD5Utils.getMD5(mUrl);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_save_image){
            //保存图片
            mSaveFilePath = BitmapUtils.saveBitmap(this, BitmapUtils.convertViewToBitmap(mPreviewSdv), mSaveFileName);
            if (TextUtils.isEmpty(mSaveFilePath)) {
                DialogUtils.showShortPromptToast(mContext, R.string.save_fail);
                return;
            }
            try {
                MediaStore.Images.Media
                        .insertImage(getContentResolver(), mSaveFilePath, mSaveFileName, AppConstant.IMAGE_MEDIA_PREVIEW_STORE_DESC);
                DialogUtils.showShortPromptToast(mContext, R.string.save_success);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                DialogUtils.showShortPromptToast(mContext, R.string.save_fail);
            }
        }
    }
}
