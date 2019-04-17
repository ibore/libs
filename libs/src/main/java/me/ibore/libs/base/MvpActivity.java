package me.ibore.libs.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.ibore.libs.R;
import me.ibore.libs.manager.ActivityManager;
import me.ibore.libs.rxbus.RxBus;
import me.ibore.libs.util.BarUtils;
import me.ibore.libs.util.ClassUtils;
import me.ibore.widget.LoadLayout;

public abstract class MvpActivity<P extends MvpPresenter> extends AppCompatActivity implements MvpView {

    protected LoadLayout loadLayout;
    protected ViewGroup rootView;
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
        if (mPresenter != null) mPresenter.onAttach(this);
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
        if (null != actionBarView) layoutParams.addRule(RelativeLayout.BELOW, actionBarView.getId());
        if (null != bottomBarView) layoutParams.addRule(RelativeLayout.ABOVE, bottomBarView.getId());
        loadLayout.setLayoutParams(layoutParams);
        rootView.addView(loadLayout);
        if (null != bottomBarView) rootView.addView(bottomBarView);
        if (null != actionBarView) rootView.addView(actionBarView);
        return rootView;
    }

    @Override
    protected void onDestroy() {
        RxBus.get().unRegister(this);
        if (mPresenter != null) mPresenter.onDetach();
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

    protected final String getStringX(int stringId) {
        return getContext().getString(stringId);
    }


    protected final Drawable getDrawableX(@DrawableRes int drawableId) {
        return ContextCompat.getDrawable(getContext(), drawableId);
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof XFragment && ((XFragment) fragment).onBackPressed()) {
                super.onBackPressed();
            }
        }
    }

}

