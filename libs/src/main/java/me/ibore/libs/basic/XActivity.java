package me.ibore.libs.basic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import me.ibore.libs.R;
import me.ibore.libs.rxbus.RxBus;
import me.ibore.libs.util.ClassUtils;
import me.ibore.libs.util.DisposablesUtils;
import me.ibore.libs.util.HandleBackUtils;
import me.ibore.widget.LoadLayout;

/**
 * description: 基类，请不要继承自它
 * author: Ibore Xie
 * date: 2018-01-18 23:50
 * website: ibore.me
 */

public abstract class XActivity<P extends XPresenter> extends AppCompatActivity implements XView {

    protected P mPresenter;
    protected LoadLayout loadLayout;
    protected ViewGroup rootView;
    protected View actionBarView;
    protected View bottomBarView;
    private Unbinder unbinder;

    protected abstract int getLayoutId();

    protected abstract void onBindView(@Nullable Bundle savedInstanceState);

    protected abstract void onBindData();

    protected View getActionBarView() {
        return null;
    }

    protected View getBottomBarView() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*BarUtils.setStatusBarAlpha(this, getColorX(android.R.color.transparent));*/
        setContentView(getLayoutView(getLayoutId()));
        unbinder = ButterKnife.bind(this);
        onBindView(savedInstanceState);
        mPresenter = ClassUtils.getClass(this, 0);
        if (null != mPresenter) mPresenter.onAttach(this);
        RxBus.get().register(this);
        onBindData();
    }

    protected View getLayoutView(int layoutId) {
        rootView = new RelativeLayout(this);
        loadLayout = new LoadLayout(this);
        bottomBarView = getBottomBarView();
        actionBarView = getActionBarView();
        loadLayout.setLoadView(R.layout.libs_layout_loading, R.layout.libs_layout_empty, R.layout.libs_layout_error);
        loadLayout.setContentView(layoutId);
        loadLayout.setOnLoadingClickListener(new LoadLayout.OnLoadClickListener() {
            @Override
            public void onEmptyClick() {
            }

            @Override
            public void onErrorClick() {
                onBindData();
            }
        });
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (null != actionBarView)
            layoutParams.addRule(RelativeLayout.BELOW, actionBarView.getId());
        if (null != bottomBarView)
            layoutParams.addRule(RelativeLayout.ABOVE, bottomBarView.getId());
        loadLayout.setLayoutParams(layoutParams);
        rootView.addView(loadLayout);
        if (null != bottomBarView) rootView.addView(bottomBarView);
        if (null != actionBarView) rootView.addView(actionBarView);
        return rootView;
    }

    @Override
    protected void onDestroy() {
        RxBus.get().unRegister(this);
        DisposablesUtils.clear(this);
        if (null != mPresenter) mPresenter.onDetach();
        unbinder.unbind();
        super.onDestroy();
    }

    protected final void setLoadLayout(@LayoutRes int loadingViewResId, @LayoutRes int emptyViewResId, @LayoutRes int errorViewResId) {
        loadLayout.setLoadView(loadingViewResId, emptyViewResId, errorViewResId);
    }

    @Override
    public LoadLayout loadLayout() {
        return loadLayout;
    }

    protected int getColorX(@ColorRes int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }

    protected Drawable getDrawableX(@DrawableRes int drawableId) {
        return ContextCompat.getDrawable(getContext(), drawableId);
    }

    protected String getStringX(int stringId) {
        return getContext().getString(stringId);
    }

    @Override
    public final XActivity getXActivity() {
        return this;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    protected Disposable addDisposable(Disposable disposable) {
        return DisposablesUtils.add(this, disposable);
    }

    protected Disposable addDisposable(Observable observable, DisposableObserver observer) {
        return DisposablesUtils.add(this, observable, observer);
    }

    protected boolean removeDisposable(Disposable disposable) {
        return DisposablesUtils.remove(this, disposable);
    }

    @Override
    public void onBackPressed() {
        if (!HandleBackUtils.handleBackPress(this)) {
            super.onBackPressed();
        }
    }
}

