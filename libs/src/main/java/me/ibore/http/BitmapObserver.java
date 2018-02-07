package me.ibore.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2018/2/6.
 */

public abstract class BitmapObserver extends HttpObserver<HttpInfo> implements HttpListener<BitmapInfo> {

    @Override
    public void onNext(HttpInfo httpInfo) {
        onProgress(httpInfo.getProgressInfo());
        if (httpInfo.getProgressInfo().getCurrent() == httpInfo.getProgressInfo().getTotal()) {
            onSuccess((BitmapInfo) httpInfo.getResponseInfo());
        }
    }

    @Override
    public void onProgress(ProgressInfo progressInfo) {

    }

    @Override
    public void onError(Throwable e) {
        Utils.OnError(this, e);
    }
}
