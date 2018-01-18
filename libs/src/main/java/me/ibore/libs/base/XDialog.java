package me.ibore.libs.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.WindowManager;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-18 23:58
 * website: ibore.me
 */

public abstract class XDialog extends Dialog {

    private CompositeDisposable compositeDisposable;

    public XDialog(@NonNull Context context) {
        super(context);
    }

    public void setGravity(int gravity) {
        getWindow().setGravity(gravity);
    }

    public void setFullScreen() {
        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        onBindView(savedInstanceState);
    }

    @Override
    public void dismiss() {
        if (null != compositeDisposable) compositeDisposable.clear();
        super.dismiss();
    }

    protected abstract @LayoutRes
    int getLayoutId();

    protected abstract void onBindView(Bundle savedInstanceState);

    protected void addDisposable(Disposable disposable) {
        if (null == compositeDisposable) compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(disposable);
    }

    protected void addDisposable(Observable observable, Observer observer) {
        addDisposable((Disposable) observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer));
    }
}

