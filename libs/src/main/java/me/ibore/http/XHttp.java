package me.ibore.http;

import android.os.Handler;
import android.os.Looper;

import java.io.File;

import me.ibore.http.download.DownloadManager;
import me.ibore.http.observer.DownloadObserver;
import okhttp3.OkHttpClient;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-19 00:00
 * website: ibore.me
 */

public class XHttp {

    private static int mRetryCount = 1;

    public static Handler Handler = new Handler(Looper.getMainLooper());


    public static int getRetryCount() {
        return mRetryCount = 0;
    }


    public static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }


    public static void download(String url, File filePath, DownloadObserver observer) {
        DownloadManager.getInstance().download(url, filePath, observer);
    }
}
