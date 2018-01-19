package me.ibore.libs.http;

import android.content.Context;
import android.content.DialogInterface;

import io.reactivex.observers.DisposableObserver;
import me.ibore.libs.XApplication;
import me.ibore.libs.util.NetworkUtils;
import me.ibore.libs.view.LoadDialog;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-19 00:03
 * website: ibore.me
 */

public abstract class XHttpObserver<T> extends DisposableObserver<T> implements DialogInterface.OnCancelListener {

    private LoadDialog loadDialog;

    public XHttpObserver() {
    }

    public XHttpObserver(Context context) {
        this(context, null);
    }

    public XHttpObserver(Context context, String message) {
        loadDialog = new LoadDialog(context, message);
        loadDialog.setOnCancelListener(this);
    }

    @Override
    protected void onStart() {
        if (null != loadDialog) loadDialog.show();
        if (!NetworkUtils.isConnected()) {
            dispose();
            onError(new XHttpException(XHttpException.NetworkNotConnected, "网络未连接"));
        }
    }

    @Override
    public void onError(Throwable e) {
        onComplete();
    }

    @Override
    public void onComplete() {
        if (null != loadDialog) loadDialog.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (!isDisposed()) dispose();
    }

}
