package me.ibore.http;
/**
 * Created by Administrator on 2018/1/23.
 */

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;

/**
 * <pre>
 * description:
 * author: Ibore Xie
 * date: 9:45 2018/1/23
 * website: ibore.me
 * </pre>
 */
public abstract class FileCallback extends HttpCallback<File> {

    private File file;

    private FileOutputStream fileOutputStream = null;
    private long bytesWritten = 0;   //当前写入字节数
    private long contentLength = 0;  //总字节长度，避免多次调用contentLength()方法
    private long lastRefreshUiTime;  //最后一次刷新的时间
    private long lastWriteBytes;     //最后一次写入字节数据
    private InputStream inputStream = null;

    public FileCallback(File file) {
        this.file = file;
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (!file.isFile()) {
            file.delete();
            file.createNewFile();
        }
        byte[] bytes = new byte[2048];
        int length;
        contentLength = response.body().contentLength();
        inputStream = response.body().byteStream();
        while ((length = inputStream.read(bytes)) != -1) {
            bytesWritten += length;
            if (null == fileOutputStream) {
                fileOutputStream = new FileOutputStream(file);
            }
            fileOutputStream.write(bytes, 0, length);
            long curTime = System.currentTimeMillis();
            if (curTime - lastRefreshUiTime >= 300 || bytesWritten == contentLength) {
                long diffTime = (curTime - lastRefreshUiTime) / 1000;
                if (diffTime == 0) diffTime += 1;
                long diffBytes = bytesWritten - lastWriteBytes;
                final long networkSpeed = diffBytes / diffTime;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onProgress(bytesWritten, contentLength, networkSpeed);
                    }
                });
                lastRefreshUiTime = System.currentTimeMillis();
                lastWriteBytes = bytesWritten;
            }
        }
        inputStream.close();
        fileOutputStream.flush();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(file);
            }
        });
    }


}
