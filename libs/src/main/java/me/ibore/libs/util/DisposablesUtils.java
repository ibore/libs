package me.ibore.libs.util;

import java.util.WeakHashMap;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public final class DisposablesUtils {

    private final static WeakHashMap<Object, CompositeDisposable> mDisposables = new WeakHashMap<>();

    public static Disposable add(Object tag, Disposable disposable) {
        CompositeDisposable compositeDisposable = mDisposables.get(tag);
        if (null == compositeDisposable) {
            compositeDisposable = new CompositeDisposable();
            mDisposables.put(tag, compositeDisposable);
        }
        compositeDisposable.add(disposable);
        return disposable;
    }

    public static Disposable add(Object tag, Observable observable, DisposableObserver observer) {
        Disposable disposable = (Disposable) observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer);
        return add(tag, disposable);
    }

    public static Disposable add(Object tag, Flowable flowable, DisposableSubscriber subscriber) {
        Disposable disposable = (Disposable) flowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber);
        return add(tag, disposable);
    }

    public static Boolean remove(Object tag, Disposable disposable) {
        CompositeDisposable compositeDisposable = mDisposables.get(tag);
        if (null != compositeDisposable) {
            return compositeDisposable.remove(disposable);
        }
        return false;
    }

    public static void clear(Object tag) {
        CompositeDisposable compositeDisposable = mDisposables.get(tag);
        if (null != compositeDisposable) {
            compositeDisposable.clear();
        }
    }

}
