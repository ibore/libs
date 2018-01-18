package me.ibore.libs.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.ibore.libs.rxbus.RxBus;
import me.ibore.libs.rxbus.Subscribe;
import me.ibore.libs.util.ActivityUtil;
import me.ibore.libs.util.ClassUtil;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-18 23:50
 * website: ibore.me
 */

public abstract class XActivity<P extends XPresenter> extends AppCompatActivity implements XView {

    protected final String TAG = getClass().getSimpleName();
    private View rootView;
    private P presenter;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.getAppManager().addActivity(this);
        rootView = getLayoutView(getLayoutId());
        setContentView(rootView);
        unbinder = ButterKnife.bind(this);
        onBindView(savedInstanceState);
        presenter = ClassUtil.getClass(this, 0);
        presenter.onAttach(this);
        RxBus.get().register(this);
        onBindData();
    }

    protected abstract @LayoutRes int getLayoutId();

    protected abstract void onBindView(Bundle savedInstanceState);

    @Subscribe(code = 100)
    protected abstract void onBindData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
        presenter.onDetach();
        unbinder.unbind();
        ActivityUtil.getAppManager().finishActivity(this);
    }

    protected View getRootView() {
        return rootView;
    }

    public P getPresenter() {
        return presenter;
    }

    protected View getLayoutView(int layoutId) {
        return getLayoutInflater().inflate(layoutId, null);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
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
}

