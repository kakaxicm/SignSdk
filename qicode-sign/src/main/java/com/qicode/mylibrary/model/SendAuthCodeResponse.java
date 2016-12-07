package com.qicode.mylibrary.model;

/**
 * Created by kakaxicm on 2015/11/29.
 */
public class SendAuthCodeResponse extends BaseResponse{

    private ResultEntity result;

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public ResultEntity getResult() {
        return result;
    }

    public static class ResultEntity {
    }
}
