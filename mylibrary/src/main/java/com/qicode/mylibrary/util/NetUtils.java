package com.qicode.mylibrary.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.qicode.mylibrary.R;
import com.qicode.mylibrary.constant.NetConstant;
import com.qicode.mylibrary.model.BaseResponse;
import com.qicode.mylibrary.task.StringToBeanTask;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by huyongsheng on 2015/3/18.
 * <p/>
 * 用于处理网络相关
 * <p/>
 * 已添加get请求相关
 */
public class NetUtils {
    private static final String TAG = NetUtils.class.getSimpleName();
    private static final int ErrorToShow = -5000;
    private static final String Success = "0";
    private static final String SuccessCode = "200";

    private static NetUtils mInstance;

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private NetUtils() {
    }

    /**
     * 使用单例模式，确保控件只有一个
     */
    public static NetUtils getInstance() {
        if (mInstance == null) {
            mInstance = new NetUtils();
        }
        return mInstance;
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public static HttpHandler<String> getNet(Context context, String url, RequestParams params,
                                             RequestCallBack<String> callBack) {
        if (isNetworkConnected(context)) {
            // 请求网络
            return new HttpUtils().send(HttpRequest.HttpMethod.GET, url, params, callBack);
        } else {
//            DialogUtils.showShortPromptToast(context, R.string.network_not_available);
            return null;
        }
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public static HttpHandler<String> postNet(Context context, String url, RequestParams params,
                                              RequestCallBack<String> callBack) {
        if (isNetworkConnected(context)) {
            // 请求网络
            return new HttpUtils().send(HttpRequest.HttpMethod.POST, url, params, callBack);
        } else {
//            DialogUtils.showShortPromptToast(context, R.string.network_not_available);
            return null;
        }
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public HttpHandler get(Context context, String url, RequestParams params, Callback callBack) {
        return send(context, HttpMethod.GET, url, params, callBack, 0, 10);
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public HttpHandler getNoCache(Context context, String url, RequestParams params, Callback callBack) {
        return send(context, HttpMethod.GET, url, params, callBack, 0, 0);
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public HttpHandler post(Context context, String url, RequestParams params, Callback callBack) {
        return send(context, HttpMethod.POST, url, params, callBack, 0, 10);
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public HttpHandler postNocache(Context context, String url, RequestParams params, Callback callBack) {
        return send(context, HttpMethod.POST, url, params, callBack, 0, 0);
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    private HttpHandler send(Context context, HttpMethod method, String url, RequestParams params, Callback callBack,
                             int cacheTime, int fileTime) {
        if(params != null){
            List<NameValuePair> queryStringParams = params.getQueryStringParams();
            if (queryStringParams != null) {
                for (NameValuePair item : queryStringParams) {
                    if (TextUtils.equals(NetConstant.KEY_CACHE_NAME_MD5, item.getName())) {
                        callBack.cacheFileName = item.getValue();//参数列表的MD5
                    }
                }
            }
        }

        if (callBack.cacheFileName == null) {
            callBack.cacheFileName = "";
        }
        callBack.cacheFileName = StringUtils.getString(url, callBack.cacheFileName);//url + 参数列表的MD5
        callBack.cacheFileName = StringUtils.toMD5(callBack.cacheFileName);//再MD5一次

        // 如果从文件获取数据
        if (fileTime > 0) {
            callBack.isFileCacheEnable = true;
            String fileCache = FileUtils.readStringFromFileCache(context, callBack.cacheFileName);
//            Log.e("cache", "读缓存:文件名"+callBack.cacheFileName+",内容:"+fileCache);
            if (!TextUtils.isEmpty(fileCache)) {
                ResponseInfo<String> info = new ResponseInfo<>(null, fileCache, true);
                if (callBack != null) {
                    callBack.onSuccess(info);
                }
            }
        }
        if (isNetworkConnected(context)) {
            // 请求网络
            return new HttpUtils().configCurrentHttpCacheExpiry(cacheTime).send(method, url, params, callBack);
        } else {
//            DialogUtils.showShortPromptToast(context, R.string.network_not_available);
            if (callBack != null) {
                String fileCache = FileUtils.readStringFromFileCache(context, callBack.cacheFileName);
                if (TextUtils.isEmpty(fileCache)) {//如果缓存没有数据则显示failed
                    callBack.onNetFailed(new HttpException(context.getString(R.string.network_not_available)));
                }
            }

        }
        return null;
    }

    /**
     * 获取网络字串，如果本地有缓存则从本地取
     */
    public static HttpHandler getStringResponseWithCache(Context context, HttpMethod method, String url, RequestParams params,
                                                         StringResponseCallback callBack,
                                                         int memoryCacheTime, boolean enableReadCache, String cacheFileName) {
        callBack.cacheFileName = cacheFileName;
        //读缓存 or 请求网络
        String fileCache = FileUtils.readStringFromFileCache(context, callBack.cacheFileName);
        if (!TextUtils.isEmpty(fileCache) && enableReadCache) {
            ResponseInfo<String> info = new ResponseInfo<>(null, fileCache, true);
            callBack.onSuccess(info);
        } else {
            return new HttpUtils().configCurrentHttpCacheExpiry(memoryCacheTime)
                    .send(method, url, params, callBack);
        }
        return null;
    }

    /**
     * 抓取字体字串时的回调，不用json解析
     */
    public static abstract class StringResponseCallback extends RequestCallBack<String> {
        private Context mContext;
        private String cacheFileName;

        public StringResponseCallback(Context context) {
            mContext = context;
            cacheFileName = "cacheFileName";
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            onStringRequestSuccess(responseInfo.result);
            String cache = FileUtils.readStringFromFileCache(mContext, cacheFileName);
            if (TextUtils.isEmpty(cache)) {
                FileUtils.writeStringToFileCache(mContext, cacheFileName, responseInfo.result);
            }
        }

        @Override
        public void onFailure(HttpException error, String msg) {
//            DialogUtils.showShortPromptToast(mContext, "获取字体信息失败");
        }

        //成功拿到字串，交给业务处理
        protected abstract void onStringRequestSuccess(String response);
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public abstract static class Callback<Response extends BaseResponse> extends RequestCallBack<String>
            implements StringToBeanTask.ConvertListener<Response> {
        private Activity mActivity;
        private Class<Response> mClassType;
        private String cacheFileName;
        private boolean isFileCacheEnable;

        public abstract void onNetSuccess(Response response);

        public void onNetFailed(HttpException e) {
        }

        public Callback(Activity activity, Class<Response> classType) {
            mActivity = activity;
            mClassType = classType;
            cacheFileName = "cacheFileName";
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<Response> task = new StringToBeanTask<>(mClassType, this);
            task.execute(responseInfo.result);
            //如果开启缓存,则将json存入缓存
            if (isFileCacheEnable) {
                FileUtils.writeStringToFileCache(mActivity, cacheFileName, responseInfo.result);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            dealError(e);
        }

        @Override
        public void onConvertSuccess(Response response) {
            if (response != null && response.getStatus() != null) {
                if (Success.equals(response.getStatus().getCode()) || SuccessCode.equals(response.getStatus().getCode())) {
                    onNetSuccess(response);
                } else {
                    HttpException e = new HttpException(ErrorToShow, response.getStatus().getDescription());
                    dealError(e);
                }
            } else {
                HttpException e = new HttpException("无法解析response");
                dealError(e);
            }
        }

        @Override
        public void onConvertFailed(String json, String desc) {
            HttpException exception;
            if (desc == null) {
                exception = new HttpException("Convert Json Failed" + json);
            } else {
                exception = new HttpException(ErrorToShow, desc);
            }
            dealError(exception);
        }

        /**
         * 处理错误的统一入口
         *
         * @param e
         */
        private void dealError(final HttpException e) {
            switch (e.getExceptionCode()) {
                case ErrorToShow:
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            DialogUtils.showShortPromptToast(mActivity, e.getMessage());
                        }
                    });
                    break;
                default:
                    break;
            }
            LogUtils.e(mActivity, TAG, e);
            onNetFailed(e);
        }
    }
}