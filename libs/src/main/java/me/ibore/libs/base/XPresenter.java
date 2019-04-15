package me.ibore.libs.base;

import androidx.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import me.ibore.libs.util.DisposablesUtils;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-18 23:52
 * website: ibore.me
 */
public abstract class XPresenter<V extends XView> {

    protected final V getView() {
        return mView;
    }

    protected V mView;

    protected void onAttach(@NonNull V view) {
        if (this.mView != null)
            throw new IllegalStateException("View " + this.mView + " is already attached. Cannot attach " + view);
        this.mView = view;
    }

    protected void onDetach() {
        DisposablesUtils.clear(this);
        if (this.mView == null) throw new IllegalStateException("View is already detached");
        mView = null;
    }

    protected final Disposable addDisposable(Disposable disposable) {
        return DisposablesUtils.add(this, disposable);
    }

    protected final Disposable addDisposable(Observable observable, DisposableObserver observer) {
        return DisposablesUtils.add(this, observable, observer);
    }

    protected final boolean removeDisposable(Disposable disposable) {
        return DisposablesUtils.remove(this, disposable);
    }

}

