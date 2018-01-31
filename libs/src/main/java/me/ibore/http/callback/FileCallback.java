package me.ibore.http.callback;
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

import me.ibore.http.XHttp;
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

    public FileCallback(File file) {
        this.file = file;
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {


    }
}
