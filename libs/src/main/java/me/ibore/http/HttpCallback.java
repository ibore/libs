package me.ibore.http;
/**
 * Created by Administrator on 2018/1/23.
 */

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;

/**
 * <pre>
 * description:
 * author: Ibore Xie
 * date: 11:47 2018/1/23
 * website: ibore.me
 * </pre>
 */
public abstract class HttpCallback<T> implements okhttp3.Callback, Callback<T> {

    protected static final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onFailure(Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onError(new HttpException(-1, e.getMessage()));
            }
        });
    }

    public void onProgress(long current, long total, long netSpeed) {

    }

}
