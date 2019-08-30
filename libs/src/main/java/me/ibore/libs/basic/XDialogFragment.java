package me.ibore.libs.basic;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import me.ibore.libs.rxbus.RxBus;
import me.ibore.libs.util.ClassUtils;
import me.ibore.libs.util.DisposablesUtils;

public abstract class XDialogFragment<P extends XPresenter> extends AppCompatDialogFragment implements XView {

    protected P mPresenter;

    protected abstract int getLayoutId();

    protected abstract void onBindView(Bundle savedInstanceState);

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onBindView(savedInstanceState);
        mPresenter = ClassUtils.getClass(this, 0);
        if (null != mPresenter) mPresenter.onAttach(this);
        RxBus.get().register(this);
        onBindData();
    }

    @Override
    public void onDestroyView() {
        RxBus.get().unRegister(this);
        DisposablesUtils.clear(this);
        if (null != mPresenter) mPresenter.onDetach();
        unBinder.unbind();
        super.onDestroyView();
    }

    @Override
    public final XActivity getXActivity() {
        return (XActivity) getActivity();
    }

    protected final int getColorX(@ColorRes int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }

    protected final String getStringX(int stringId) {
        return getContext().getString(stringId);
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
