package me.ibore.libs.http;

import android.content.Context;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLHandshakeException;

import me.ibore.libs.BuildConfig;
import me.ibore.libs.R;
import me.ibore.libs.basic.XObserver;
import me.ibore.libs.exception.ClientException;
import me.ibore.libs.exception.HttpException;
import me.ibore.libs.exception.ServerException;
import me.ibore.libs.util.Utils;
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
        } else if (e instanceof ConnectException) {
            onError(new HttpException(-1, Utils.getApp().getString(R.string.libs_connect_exception)));
        }  else if (e instanceof SSLHandshakeException) {
            onError(new HttpException(-1, Utils.getApp().getString(R.string.libs_ssl_handshake_exception)));
        } else if (e instanceof IOException) {
            if (e instanceof SocketTimeoutException) {
                onError(new HttpException(-1, Utils.getApp().getString(R.string.libs_socket_timeout_exception)));
            } else {
                onError(new HttpException(-1, Utils.getApp().getString(R.string.libs_io_exception)));
            }
        } else if (e instanceof JsonParseException || e instanceof JSONException) {
            onError(new ClientException(Utils.getApp().getString(R.string.libs_json_parse_exception)));
        } else if (e instanceof ServerException) {
            onError((Exception) e);
        } else if (e instanceof ClientException) {
            onError((Exception) e);
        } else {
            onError(new ClientException(Utils.getApp().getString(R.string.libs_client_exception)));
        }
    }
}
