package com.qicode.mylibrary.model;

/**
 * Created by star on 16/2/21.
 * 一行两列显示专家签名
 */
public class ExpertSignLineItem {
    SignListResponse.ResultEntity.ExpertSignsEntity mLeftExpertSignItem;
    SignListResponse.ResultEntity.ExpertSignsEntity mRightExpertSignItem;

    public SignListResponse.ResultEntity.ExpertSignsEntity getLeftExpertSignItem() {
        return mLeftExpertSignItem;
    }

    public void setLeftExpertSignItem(SignListResponse.ResultEntity.ExpertSignsEntity mLeftExpertSignItem) {
        this.mLeftExpertSignItem = mLeftExpertSignItem;
    }

    public SignListResponse.ResultEntity.ExpertSignsEntity getRightExpertSignItem() {
        return mRightExpertSignItem;
    }

    public void setRightExpertSignItem(SignListResponse.ResultEntity.ExpertSignsEntity mRightExpertSignItem) {
        this.mRightExpertSignItem = mRightExpertSignItem;
    }
}
