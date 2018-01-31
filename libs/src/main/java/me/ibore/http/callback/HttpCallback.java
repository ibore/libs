package me.ibore.http.callback;

import java.io.IOException;

import me.ibore.http.HttpException;
import me.ibore.http.XHttp;
import me.ibore.http.progress.ProgressInfo;
import me.ibore.http.progress.ProgressListener;
import okhttp3.Call;

/**
 * <pre>
 * description:
 * author: Ibore Xie
 * date: 11:47 2018/1/23
 * website: ibore.me
 * </pre>
 */
public abstract class HttpCallback<T> implements okhttp3.Callback, Callback<T>, ProgressListener {

    private int mRetryCount = XHttp.getRetryCount();

    @Override
    public void onProgress(ProgressInfo progressInfo) {

    }

    @Override
    public void onFailure(Call call, final IOException e) {
        if (mRetryCount > 0) {
            mRetryCount--;
            call.enqueue(this);
        } else {
            XHttp.Handler.post(new Runnable() {
                @Override
                public void run() {
                    onError(new HttpException(-1, e.getMessage()));
                }
            });
        }
    }


}
