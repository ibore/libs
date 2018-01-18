package me.ibore.libs.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.ibore.libs.rxbus.RxBus;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-18 23:57
 * website: ibore.me
 */

public abstract class XFragment extends Fragment implements XView {

    protected final String TAG = getClass().getSimpleName();
    protected Unbinder unBinder;
    private CompositeDisposable compositeDisposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  getLayoutView(getLayoutId());
        unBinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    protected abstract int getLayoutId();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        onBindView(savedInstanceState);
        RxBus.get().register(this);
    }

    protected abstract void onBindView(Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        if (null != compositeDisposable) compositeDisposable.clear();
        unBinder.unbind();
        super.onDestroyView();
    }

    public View getLayoutView(int layoutId) {
        return getLayoutInflater().inflate(layoutId, null);
    }

    public int getColorX(int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }

    public Drawable getDrawableX(int drawableId) {
        return ContextCompat.getDrawable(getContext(), drawableId);
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

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
