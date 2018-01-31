package me.ibore.http.callback;
/**
 * Created by Administrator on 2018/1/23.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * date: 11:17 2018/1/23
 * website: ibore.me
 * </pre>
 */
public abstract class BitmapCallback extends HttpCallback<Bitmap>  {

    @Override
    public void onResponse(Call call, Response response) throws IOException {

    }
}
