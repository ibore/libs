package me.ibore.libs.basic;

import android.content.Context;
import android.content.DialogInterface;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.observers.DisposableObserver;
import me.ibore.libs.BuildConfig;
import me.ibore.libs.R;
import me.ibore.libs.dialog.LoadDialog;
import me.ibore.libs.exception.ClientException;
import me.ibore.libs.exception.HttpException;
import me.ibore.libs.exception.ServerException;
import me.ibore.libs.util.Utils;
import me.ibore.widget.LoadLayout;

public abstract class XObserver<T> extends DisposableObserver<T>implements DialogInterface.OnDismissListener {

    private LoadLayout mLoadLayout = null;
    private LoadDialog mLoadDialog = null;

    public XObserver() {
    }

    public XObserver(Context context) {
        this(context, null);
    }

    public XObserver(Context context, String message) {
        mLoadDialog = new LoadDialog(context, message);
        mLoadDialog.setOnDismissListener(this);
    }

    public XObserver(LoadLayout loadLayout) {
        this.mLoadLayout = loadLayout;
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLoading();
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        showError();
        //如果你想自己处理这些信息，请重写handleExceptions()这个方法
        handleExceptions(e);
    }

    @Override
    public void onComplete() {
        showContent();
    }

    protected void showLoading() {
        if (null != mLoadDialog) mLoadDialog.show();
        if (null != mLoadLayout) mLoadLayout.showLoadingView();
    }


    protected void showContent() {
        if (null != mLoadDialog) mLoadDialog.dismiss();
        if (null != mLoadLayout) mLoadLayout.showContentView();
    }

    protected void showEmpty() {
        if (null != mLoadDialog) mLoadDialog.dismiss();
        if (null != mLoadLayout) mLoadLayout.showEmptyView();
    }

    protected void showError() {
        if (null != mLoadDialog) mLoadDialog.dismiss();
        if (null != mLoadLayout) mLoadLayout.showErrorView();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (!isDisposed()) dispose();
    }

    protected void handleExceptions(Throwable e) {
        if (BuildConfig.DEBUG) e.printStackTrace();
        if (e instanceof retrofit2.HttpException) {
            retrofit2.HttpException he = (retrofit2.HttpException) e;
            onError(new HttpException(he.code(), he.message()));
        } else if (e instanceof ConnectException) {
            onError(new HttpException(-1, Utils.getApp().getString(R.string.connect_exception)));
        }  else if (e instanceof SSLHandshakeException) {
            onError(new HttpException(-1, Utils.getApp().getString(R.string.ssl_handshake_exception)));
        } else if (e instanceof IOException) {
            if (e instanceof SocketTimeoutException) {
                onError(new HttpException(-1, Utils.getApp().getString(R.string.socket_timeout_exception)));
            } else {
                onError(new HttpException(-1, Utils.getApp().getString(R.string.io_exception)));
            }
        } else if (e instanceof JsonParseException || e instanceof JSONException) {
            onError(new ClientException(Utils.getApp().getString(R.string.json_parse_exception)));
        } else if (e instanceof ServerException) {
            onError((Exception) e);
        } else if (e instanceof ClientException) {
            onError((Exception) e);
        } else {
            onError(new ClientException(Utils.getApp().getString(R.string.client_exception)));
        }
    }

    public abstract void onSuccess(T t);

    public abstract void onError(Exception e);
}
