package me.ibore.libs.base;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-18 23:52
 * website: ibore.me
 */
public abstract class XPresenter<V extends XView> {

    protected V getView() {
        return view;
    }

    private V view;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void onAttach(@NonNull V view) {
        if (this.view != null)
            throw new IllegalStateException("View " + this.view + " is already attached. Cannot attach " + view);
        this.view = view;
    }

    protected void onDetach() {
        compositeDisposable.clear();
        if (this.view == null)
            throw new IllegalStateException("View is already detached");
        view = null;
    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected void addDisposable(Observable observable, Observer observer) {
        addDisposable((Disposable) observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer));
    }
}

