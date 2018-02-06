package me.ibore.http;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.ObjectOutput;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;

import static me.ibore.http.Utils.CHECKNULL;
import static me.ibore.http.Utils.createHttpInfo;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-19 00:00
 * website: ibore.me
 */

public class XHttp {

    public static final int RETRY_COUNT = 1;

    public static final long REFRESH_TIME = 300;

    public static Handler Handler = new Handler(Looper.getMainLooper());

    public static OkHttpClient getOkHttpClient() {
        HttpInterceptor httpInterceptor = new HttpInterceptor("HTTP");
        httpInterceptor.setPrintLevel(HttpInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(httpInterceptor).build();
    }

    public static void download(String url, File fileDirs, DownloadObserver downloadObserver) {
        CHECKNULL(getOkHttpClient());
        Observable.just(url)
                .flatMap(s -> Observable.just(createHttpInfo(s, fileDirs)))
                .flatMap(httpInfo -> Observable.create(new DownloadSubscribe(httpInfo)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(downloadObserver);
    }

    public static void download(String url, File fileDirs, BitmapObserver bitmapObserver) {
        CHECKNULL(getOkHttpClient());
        Observable.just(url)
                .flatMap(s -> Observable.just(createHttpInfo(s, fileDirs)))
                .flatMap(httpInfo -> Observable.create(new DownloadSubscribe(httpInfo)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(bitmapObserver);
    }

    public static void get(String url) {

    }

    /**
     * 取消单个请求
     * @param tag
     */
    public static void cancel(Object tag) {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消全部请求
     */
    public static void cancelAll() {
        getOkHttpClient().dispatcher().cancelAll();
    }





}
