package me.ibore.http;

import java.io.File;
import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

import static me.ibore.http.XHttp.getOkHttpClient;

/**
 * Created by ibore on 18-2-7.
 */

class Utils {

    public static void CHECKNULL(Object object) {
        if (null == object) {
            throw new NullPointerException("please init XHttp");
        }
    }

    public static HttpInfo createDownloadInfo(String url, File fileDirs) throws IOException {
        HttpInfo<DownloadInfo> httpInfo = new HttpInfo<>();
        ProgressInfo progressInfo = new ProgressInfo();
        progressInfo.setMode(ProgressInfo.DOWNLOAD);
        progressInfo.setUrl(url);
        progressInfo.setTotal(getContentLength(url));
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setUrl(url);
        downloadInfo.setFileName(url.substring(url.lastIndexOf("/")));
        downloadInfo.setFile(new File(fileDirs, downloadInfo.getFileName()));
        httpInfo.setProgressInfo(progressInfo);
        httpInfo.setResponseInfo(downloadInfo);
        return httpInfo;
    }

    public static HttpInfo createBitmapInfo(String url, File fileDirs) throws IOException {
        HttpInfo<BitmapInfo> httpInfo = new HttpInfo<>();
        ProgressInfo progressInfo = new ProgressInfo();
        progressInfo.setMode(ProgressInfo.DOWNLOAD);
        progressInfo.setUrl(url);
        progressInfo.setTotal(getContentLength(url));
        BitmapInfo bitmapInfo = new BitmapInfo();
        bitmapInfo.setUrl(url);
        bitmapInfo.setFileName(url.substring(url.lastIndexOf("/")));
        bitmapInfo.setFile(new File(fileDirs, bitmapInfo.getFileName()));
        httpInfo.setProgressInfo(progressInfo);
        httpInfo.setResponseInfo(bitmapInfo);
        return httpInfo;
    }

    public static HttpInfo<StringInfo> createStringInfo(String url) {
        HttpInfo<StringInfo> httpInfo = new HttpInfo<>();
        ProgressInfo progressInfo = new ProgressInfo();
        progressInfo.setMode(ProgressInfo.DOWNLOAD);
        progressInfo.setUrl(url);
        progressInfo.setTotal(getContentLength(url));
        StringInfo stringInfo = new StringInfo();
        stringInfo.setUrl(url);
        httpInfo.setProgressInfo(progressInfo);
        httpInfo.setResponseInfo(stringInfo);
        return httpInfo;
    }


    public static long getContentLength(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = getOkHttpClient().newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.close();
                return contentLength == 0 ? -1 : contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void OnError(HttpListener httpListener, Throwable e) {
        if (e instanceof HttpException) {
            httpListener.onError((HttpException) e);
        } else {
            httpListener.onError(new HttpException(-1, e.getMessage()));
        }
    }


}
