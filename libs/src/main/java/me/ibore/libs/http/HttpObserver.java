package me.ibore.libs.http;

import android.content.Context;

import com.google.gson.JsonParseException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import me.ibore.libs.BuildConfig;
import me.ibore.libs.base.XObserver;
import me.ibore.libs.exception.ClientException;
import me.ibore.libs.exception.HttpException;
import me.ibore.libs.exception.ServerException;
import me.ibore.widget.LoadLayout;

public abstract class HttpObserver<T> extends XObserver<T> implements HttpListener<T> {

    public HttpObserver() {
    }

    public HttpObserver(Context context) {
        super(context);
    }

    public HttpObserver(Context context, String message) {
        super(context, message);
    }

    public HttpObserver(LoadLayout loadLayout) {
        super(loadLayout);
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        //如果你想自己处理这些信息，请重写handleExceptions()这个方法
        handleExceptions(e);
    }

    protected void handleExceptions(Throwable e) {
        if (BuildConfig.DEBUG) e.printStackTrace();
        if (e instanceof retrofit2.HttpException) {
            retrofit2.HttpException he = (retrofit2.HttpException) e;
            onError(new HttpException(he.code(), he.message()));
        } else if (e instanceof IOException) {
            if (e instanceof SocketTimeoutException || e instanceof TimeoutException) {
                onError(new HttpException(-1, "连接超时，请稍后再试"));
            } else {
                onError(new HttpException(-1, "网络请求失败，请稍后再试"));
            }
        } else if (e instanceof JsonParseException) {
            onError(new ClientException("解析出错，工程师正在修复中"));
        } else if (e instanceof ServerException) {
            onError((Exception) e);
        } else if (e instanceof ClientException) {
            onError((Exception) e);
        } else {
            onError(new ClientException("程序出错了"));
        }
    }
}
