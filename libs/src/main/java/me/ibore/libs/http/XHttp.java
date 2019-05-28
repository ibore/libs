package me.ibore.libs.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.ibore.libs.R;
import me.ibore.libs.exception.ClientException;
import me.ibore.libs.exception.HttpException;
import me.ibore.libs.util.CloseUtils;
import me.ibore.libs.util.EncryptUtils;
import me.ibore.libs.util.Utils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public final class XHttp {

    private static final int UI_REFRESH_TIME = 300;

    private static OkHttpClient okHttpClient;

    public static synchronized OkHttpClient getDefaultClient() {
        if (null == okHttpClient) {
            synchronized (XHttp.class) {
                okHttpClient = new OkHttpClient.Builder()
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .addInterceptor(new HttpInterceptor())
                        .build();
            }
        }
        return okHttpClient;
    }

    public static Observable<File> download(String url, OkHttpClient client, File fileDir, ProgressListener listener) {
        return download(url, client, fileDir.getAbsolutePath() + "/" + getFileName(url), listener);
    }

    public static Observable<File> download(String url, OkHttpClient client, String tempFilePath, ProgressListener listener) {
        return Observable.create(emitter -> {
            emitter.onNext(downloads(url, client, tempFilePath, listener));
            emitter.onComplete();
        });
    }

    public static File downloads(String url, OkHttpClient client, String tempFilePath, ProgressListener listener) throws IOException {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(tempFilePath)) {
            throw new ClientException(Utils.getApp().getString(R.string.libs_parameter_cannot_be_null));
        }
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        ProgressResponseBody progressResponseBody = ProgressResponseBody.create(response.body(), listener, true, UI_REFRESH_TIME);
        BufferedSink sink = null;
        Buffer buffer = null;
        BufferedSource source = null;
        if (response.isSuccessful()) {
            try {
                File tempFile = new File(tempFilePath);
                Progress progress = new Progress();
                progress.setContentLength(progressResponseBody.contentLength());
                if (tempFile.length() == progress.getContentLength()) {
                    progress.setEachBytes(0L);
                    progress.setCurrentBytes(progress.getContentLength());
                    progress.setIntervalTime(0L);
                    progress.setUsedTime(0L);
                    progress.setFinish(true);
                    XHttp.runOnUiThread(() -> listener.onProgress(progress));
                } else {
                    tempFile.createNewFile();
                    sink = Okio.buffer(Okio.sink(tempFile));
                    buffer = sink.buffer();
                    source = progressResponseBody.source();
                    while (source.read(buffer, 200 * 1024) != -1) {
                        sink.emit();
                    }
                    source.close();
                    sink.close();
                }
                return tempFile;
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw new ClientException(Utils.getApp().getString(R.string.libs_file_download_failed), e);
            } finally {
                CloseUtils.closeIOQuietly(sink, buffer, source);
            }
        } else {
            throw new HttpException(response.code(), response.message());
        }
    }

    /**
     * 通过 ‘？’ 和 ‘/’ 判断文件名
     * http://mavin-manzhan.oss-cn-hangzhou.aliyuncs.com/1486631099150286149.jpg?x-oss-process=image/watermark,image_d2F0ZXJtYXJrXzIwMF81MC5wbmc
     */
    public static String getFileName(String url) {
        String filename = null;
        String[] strings = url.split("/");
        for (String string : strings) {
            if (string.contains("?")) {
                int endIndex = string.indexOf("?");
                if (endIndex != -1) {
                    filename = string.substring(0, endIndex);
                    return filename;
                }
            }
        }
        if (strings.length > 0) {
            filename = strings[strings.length - 1];
        }
        if (TextUtils.isEmpty(filename)) {
            filename = EncryptUtils.encryptMD5ToString(url) + ".temp";
        }
        return filename;
    }

    private static final Handler mDelivery = new Handler(Looper.getMainLooper());

    static void runOnUiThread(Runnable runnable) {
        mDelivery.post(runnable);
    }

}
