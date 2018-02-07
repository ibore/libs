package me.ibore.http;

/**
 * Created by Administrator on 2018/2/6.
 */

public abstract class GsonObserver extends HttpObserver<HttpInfo> implements HttpListener<DownloadInfo> {

    @Override
    public void onNext(HttpInfo httpInfo) {
        onProgress(httpInfo.getProgressInfo());
        if (httpInfo.getProgressInfo().getCurrent() == httpInfo.getProgressInfo().getTotal()) {
            onSuccess((DownloadInfo) httpInfo.getResponseInfo());
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
