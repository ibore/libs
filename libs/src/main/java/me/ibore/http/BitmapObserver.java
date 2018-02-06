package me.ibore.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import me.ibore.http.listener.HttpListener;

/**
 * Created by Administrator on 2018/2/6.
 */

public abstract class BitmapObserver extends HttpObserver<HttpInfo> implements HttpListener<Bitmap> {

    @Override
    public void onNext(HttpInfo httpInfo) {
        onProgress(httpInfo.getProgressInfo());
        if (httpInfo.getProgressInfo().getCurrent() == httpInfo.getProgressInfo().getTotal()) {
            String fileName = ((DownloadInfo) httpInfo.getResponseInfo()).getFile().getAbsolutePath();
            onSuccess(BitmapFactory.decodeFile(fileName));
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
