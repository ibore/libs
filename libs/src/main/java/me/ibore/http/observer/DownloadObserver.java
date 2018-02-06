package me.ibore.http.observer;

import me.ibore.http.HttpException;
import me.ibore.http.ProgressInfo;
import me.ibore.http.listener.HttpListener;
import me.ibore.http.listener.ProgressListener;

/**
 * Created by Administrator on 2018/2/6.
 */

public abstract class DownloadObserver extends HttpObserver<ProgressInfo> implements ProgressListener, HttpListener<String> {

    @Override
    public void onNext(ProgressInfo httpInfo) {
        if (httpInfo.getCurrent() == httpInfo.getTotal()) {
            onSuccess((String) httpInfo.getData());
        } else {
            onProgress(httpInfo);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            onError((HttpException) e);
        } else {
            onError(new HttpException(-1, e.getMessage()));
        }
    }

}
