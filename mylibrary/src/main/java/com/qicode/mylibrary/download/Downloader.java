package com.qicode.mylibrary.download;

import android.content.Context;
import android.net.Proxy;
import android.os.Build;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;

public class Downloader {

    private Context mCtx;

    public Downloader(Context ctx) {
        this.mCtx = ctx;
    }

    public void downFile(String url, HashMap<String, String> headers, DownloadProcessor downloadProcessor, boolean isGet)
            throws HttpException {
        HttpClient httpClient = null;
        try {
            HttpParams params = new BasicHttpParams();
            if (DownloadNetConfig.CURRENT_NETWORK_STATE_TYPE == DownloadNetConfig.NETWORK_STATE_CMWAP
                    || DownloadNetConfig.CURRENT_NETWORK_STATE_TYPE == DownloadNetConfig.NETWORK_STATE_CTWAP) {
                String proxyHost;
                int proxyPort;
                if (Build.VERSION.SDK_INT >= 13) {
                    proxyHost = System.getProperties().getProperty("http.proxyHost");
                    proxyPort = Integer.parseInt(System.getProperties().getProperty("http.proxyPort"));
                } else {
                    proxyHost = Proxy.getHost(mCtx);
                    proxyPort = Proxy.getPort(mCtx);
                }
                if (proxyHost != null) {
                    HttpHost proxy = new HttpHost(proxyHost, proxyPort);
                    params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                }
            }
            HttpConnectionParams.setConnectionTimeout(params, DownloadNetConfig.HTTP_CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, DownloadNetConfig.HTTP_SO_TIMEOUT);
            HttpConnectionParams.setSocketBufferSize(params, DownloadNetConfig.HTTP_SOCKET_BUFFER_SIZE);
            HttpClientParams.setRedirecting(params, false);
            httpClient = new DefaultHttpClient(params);
            HttpResponse response;
            HttpContext httpContext = new BasicHttpContext();
            if (isGet) {
                HttpGet hg = new HttpGet(url);
                // 头部处理
                if (headers != null && headers.size() > 0) {
                    Iterator<String> iterator = headers.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        String value = headers.get(key);
                        hg.addHeader(key, value);
                    }
                }
                response = httpClient.execute(hg, httpContext);
            } else {
                HttpPost hp = new HttpPost(url);
                if (headers != null && headers.size() > 0) {
                    Iterator<String> iterator = headers.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        String value = headers.get(key);
                        hp.addHeader(key, value);
                    }
                }
                response = httpClient.execute(hp, httpContext);
            }

            if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 206) {
                HttpEntity entity = response.getEntity();
                downloadProcessor.processStream(entity.getContent(), entity.getContentLength(), entity.getContentEncoding(), null);
            } else if (response.getStatusLine().getStatusCode() == 301
                    || response.getStatusLine().getStatusCode() == 302) {
                String redirectURL = response.getHeaders("location")[0].getValue();
                downloadProcessor.processStream(null, -1, null, redirectURL);
                downFile(redirectURL, headers, downloadProcessor, isGet);
            }
        } catch (SocketTimeoutException e) {
            throw new HttpException(e.getMessage());
        } catch (IOException e) {
            throw new HttpException(e.getMessage());
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
    }

    public interface DownloadProcessor {
        void processStream(InputStream stream, long totalSize, Header encoding, String newURL);
    }
}
