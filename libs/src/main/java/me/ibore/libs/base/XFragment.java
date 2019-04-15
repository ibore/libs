package me.ibore.libs.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import me.ibore.libs.rxbus.RxBus;
import me.ibore.libs.util.DisposablesUtils;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-18 23:57
 * website: ibore.me
 */

public abstract class XFragment extends Fragment {

    private boolean isPrepared;
    private boolean isVisible;
    private boolean isInitialized;

    protected abstract int getLayoutId();

    protected abstract void onBindView(Bundle savedInstanceState);

    /**
     * 这个是懒加载模式的
     */
    protected void onBindData() { }

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisible = isVisibleToUser;
        if (isVisible && isPrepared && !isInitialized) {
            isInitialized = true;
            onBindData();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onBindView(savedInstanceState);
        RxBus.get().register(this);
        isPrepared = true;
        if (isVisible && !isInitialized) {
            isInitialized = true;
            onBindData();
        }
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
