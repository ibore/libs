package me.ibore.libs.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.ibore.libs.R;
import me.ibore.libs.manager.ActivityManager;
import me.ibore.libs.rxbus.RxBus;
import me.ibore.libs.util.BarUtils;
import me.ibore.libs.util.ClassUtils;
import me.ibore.widget.LoadLayout;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-18 23:50
 * website: ibore.me
 */

public abstract class XActivity<P extends XPresenter> extends AppCompatActivity implements XView {

    protected LoadLayout loadLayout;
    protected RelativeLayout rootView;
    protected View actionBarView;
    protected View bottomBarView;
    protected P mPresenter;
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
        ActivityManager.getAppManager().addActivity(this);
        BarUtils.setStatusBarAlpha(this, getColorX(android.R.color.transparent));
        setContentView(getLayoutView(getLayoutId()));
        unbinder = ButterKnife.bind(this);
        onBindView(savedInstanceState);
        mPresenter = ClassUtils.getClass(this, 0);
        mPresenter.onAttach(this);
        RxBus.get().register(this);
        onBindData();
    }

    protected View getLayoutView(int layoutId) {
        rootView = new RelativeLayout(this);
        loadLayout = new LoadLayout(this);
        loadLayout.setLoadView(R.layout.layout_loading, R.layout.layout_empty, R.layout.layout_error);
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
        actionBarView = getActionBarView();
        bottomBarView = getBottomBarView();
        if (null != actionBarView) {
            rootView.addView(actionBarView);
            layoutParams.addRule(RelativeLayout.BELOW, actionBarView.getId());
        }
        if (null != bottomBarView) {
            rootView.addView(bottomBarView);
            layoutParams.addRule(RelativeLayout.ABOVE, bottomBarView.getId());
        }
        loadLayout.setLayoutParams(layoutParams);
        rootView.addView(loadLayout);
        return rootView;
    }

    @Override
    protected void onDestroy() {
        RxBus.get().unRegister(this);
        mPresenter.onDetach();
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public LoadLayout loadLayout() {
        return loadLayout;
    }

    protected final int getColorX(@ColorRes int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }

    protected final Drawable getDrawableX(@DrawableRes int drawableId) {
        return ContextCompat.getDrawable(getContext(), drawableId);
    }

}

