package me.ibore.http;

import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.ibore.http.HttpInfo;
import me.ibore.http.ProgressInfo;
import me.ibore.http.XHttp;
import me.ibore.http.DownloadInfo;
import me.ibore.libs.util.CloseUtils;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ibore on 18-2-6.
 */

public class DownloadSubscribe implements ObservableOnSubscribe<HttpInfo> {

    private HttpInfo<DownloadInfo> httpInfo;

    public DownloadSubscribe(HttpInfo<DownloadInfo> httpInfo) {
        this.httpInfo = httpInfo;
    }

    @Override
    public void subscribe(ObservableEmitter<HttpInfo> e) throws Exception {
        ProgressInfo progressInfo = httpInfo.getProgressInfo();
        DownloadInfo downloadInfo  = httpInfo.getResponseInfo();
        if (downloadInfo.getFile().exists()) {
            progressInfo.setCurrent(downloadInfo.getFile().length());
        }
        if (progressInfo.getCurrent() == progressInfo.getTotal()) {
            e.onNext(httpInfo);
        } else {
            if (progressInfo.getCurrent() > progressInfo.getTotal()) {
                progressInfo.setCurrent(0);
                downloadInfo.getFile().createNewFile();
            }
            String url = downloadInfo.getUrl();
            long bytesWritten = progressInfo.getCurrent();
            long contentLength = progressInfo.getTotal();
            e.onNext(httpInfo);
            Request request = new Request.Builder()
                    .addHeader("RANGE", "bytes=" + bytesWritten + "-" + contentLength)
                    .url(url)
                    .tag(url)
                    .build();
            Response response  =  XHttp.getOkHttpClient().newCall(request).execute();
            InputStream is = null;
            FileOutputStream fileOutputStream = null;
            try {
                is = response.body().byteStream();
                fileOutputStream = new FileOutputStream(downloadInfo.getFile(), true);
                byte[] buffer = new byte[2048];//缓冲数组2kB
                int len;
                long lastRefreshUiTime = 0;
                while ((len = is.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                    bytesWritten += len;
                    long curTime = System.currentTimeMillis();
                    long lastWriteBytes = 0;
                    if (curTime - lastRefreshUiTime >= XHttp.REFRESH_TIME || bytesWritten == contentLength) {
                        long diffTime = (curTime - lastRefreshUiTime) / 1000;
                        if (diffTime == 0) diffTime += 1;
                        long diffBytes = bytesWritten - lastWriteBytes;
                        final long networkSpeed = diffBytes / diffTime;
                        lastRefreshUiTime = System.currentTimeMillis();
                        lastWriteBytes = bytesWritten;
                        progressInfo.setSpeed(networkSpeed);
                        progressInfo.setCurrent(bytesWritten);
                        progressInfo.setProgress((int) (bytesWritten * 10000 / contentLength));
                        e.onNext(httpInfo);
                    }
                }
                fileOutputStream.flush();
            } finally {
                CloseUtils.closeIO(is, fileOutputStream);
            }
            e.onComplete();//完成
        }

    }


}
