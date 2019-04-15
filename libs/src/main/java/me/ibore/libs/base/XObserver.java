package me.ibore.libs.base;

import android.content.Context;
import android.content.DialogInterface;

import io.reactivex.observers.DisposableObserver;
import me.ibore.libs.dialog.LoadDialog;
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
    public void onError(Throwable e) {
        showError();
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
}
