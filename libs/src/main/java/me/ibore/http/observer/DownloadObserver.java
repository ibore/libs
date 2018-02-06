package me.ibore.http.observer;

import java.io.File;

import me.ibore.http.HttpException;
import me.ibore.http.ProgressInfo;
import me.ibore.http.listener.HttpListener;
import me.ibore.http.listener.ProgressListener;

/**
 * Created by Administrator on 2018/2/6.
 */

public abstract class DownloadObserver extends HttpObserver<ProgressInfo> implements ProgressListener, HttpListener<File> {

    @Override
    public void onNext(ProgressInfo httpInfo) {
        if (httpInfo.getCurrent() == httpInfo.getTotal()) {
            onSuccess((File) httpInfo.getData());
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
