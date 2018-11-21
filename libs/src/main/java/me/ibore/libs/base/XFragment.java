package me.ibore.libs.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
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
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import me.ibore.libs.rxbus.RxBus;
import me.ibore.libs.util.DisposablesUtils;
import me.ibore.widget.LoadLayout;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-18 23:57
 * website: ibore.me
 */

public abstract class XFragment extends Fragment {

    protected abstract int getLayoutId();

    protected abstract void onBindView(Bundle savedInstanceState);

    private Unbinder unBinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = getLayoutView(getLayoutId());
        unBinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private View getLayoutView(int layoutId) {
        View rootView = getLayoutInflater().inflate(layoutId, null);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBindView(savedInstanceState);
        RxBus.get().register(this);
    }

    @Override
    public void onDestroyView() {
        RxBus.get().unRegister(this);
        DisposablesUtils.clear(this);
        unBinder.unbind();
        super.onDestroyView();
    }

    public Boolean onBackPressed() {
        return true;
    }

    protected final int getColorX(@ColorRes int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }

    protected final Drawable getDrawableX(@DrawableRes int drawableId) {
        return ContextCompat.getDrawable(getContext(), drawableId);
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
