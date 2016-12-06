package com.qicode.mylibrary.task;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.qicode.mylibrary.model.BaseResponse;

/**
 * Created by star on 15/6/5.
 * <p/>
 * 根据string返回bean
 */
public class StringToBeanTask<T> extends AsyncTask<String, Integer, T> {
    private Class<T> mClassType;
    private ConvertListener<T> mListener;
    private String mJsonString;
    private String mDesc;

    public interface ConvertListener<T> {
        void onConvertSuccess(T response);

        void onConvertFailed(String json, String desc);
    }

    public StringToBeanTask(Class<T> classType, ConvertListener<T> listener) {
        mClassType = classType;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected T doInBackground(String... params) {
        T t = null;
        if (params.length > 0) {
            Gson gson = new Gson();
            mJsonString = params[0];
            try {
                t = gson.fromJson(mJsonString, mClassType);
            } catch (Exception e) {
                try {
                    BaseResponse baseResponse = gson.fromJson(mJsonString, BaseResponse.class);
                    mDesc = baseResponse.getStatus().getDescription();
                } catch (Exception sub_e) {
                    sub_e.printStackTrace();
                }
                return null;
            }
        }
        return t;
    }

    @Override
    protected void onPostExecute(T t) {
        if (t == null) {
            mListener.onConvertFailed(mJsonString, mDesc);
        } else {
            mListener.onConvertSuccess(t);
        }
    }
}
