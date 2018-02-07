package me.ibore.http;

import android.graphics.BitmapFactory;

import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.ibore.libs.util.CloseUtils;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/2/7.
 */

public class BitmapSubscribe implements ObservableOnSubscribe<HttpInfo> {

    private HttpInfo<BitmapInfo> httpInfo;

    public BitmapSubscribe(HttpInfo<BitmapInfo> httpInfo) {
        this.httpInfo = httpInfo;
    }

    @Override
    public void subscribe(ObservableEmitter<HttpInfo> e) throws Exception {
        ProgressInfo progressInfo = httpInfo.getProgressInfo();
        BitmapInfo bitmapInfo  = httpInfo.getResponseInfo();
        if (bitmapInfo.getFile().exists()) {
            progressInfo.setCurrent(bitmapInfo.getFile().length());
        }
        if (progressInfo.getCurrent() == progressInfo.getTotal()) {
            bitmapInfo.setBitmap(BitmapFactory.decodeFile(bitmapInfo.getFile().getAbsolutePath()));
            e.onNext(httpInfo);
        } else {
            if (progressInfo.getCurrent() > progressInfo.getTotal()) {
                progressInfo.setCurrent(0);
                bitmapInfo.getFile().createNewFile();
            }
            String url = bitmapInfo.getUrl();
            long bytesWritten = progressInfo.getCurrent();
            long contentLength = progressInfo.getTotal();
            e.onNext(httpInfo);
            Request request = new Request.Builder()
                    .addHeader("RANGE", "bytes=" + bytesWritten + "-" + contentLength)
                    .url(url)
                    .tag(url)
                    .build();
            Response response  =  XHttp.getOkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream is = null;
                FileOutputStream fileOutputStream = null;
                try {
                    is = response.body().byteStream();
                    fileOutputStream = new FileOutputStream(bitmapInfo.getFile(), true);
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
                            bitmapInfo.setBitmap(BitmapFactory.decodeFile(bitmapInfo.getFile().getAbsolutePath()));
                            e.onNext(httpInfo);
                        }
                    }
                    fileOutputStream.flush();
                } finally {
                    CloseUtils.closeIO(is, fileOutputStream);
                }
            } else {
                e.onError(new HttpException(response.code(), response.message()));
            }
        }
        e.onComplete();//完成
    }


}
