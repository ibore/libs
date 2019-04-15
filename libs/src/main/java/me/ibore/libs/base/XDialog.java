package me.ibore.libs.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
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
 * date: 2018-01-18 23:58
 * website: ibore.me
 */

public abstract class XDialog extends Dialog {

    public XDialog(Context context) {
        this(context, 0);
    }

    public XDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected abstract int getLayoutId();

    protected abstract void onBindView(Bundle savedInstanceState);

    private Unbinder unBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unBinder = ButterKnife.bind(this);
        onBindView(savedInstanceState);
        RxBus.get().register(this);
    }

    @Override
    public void dismiss() {
        RxBus.get().unRegister(this);
        DisposablesUtils.clear(this);
        unBinder.unbind();
        super.dismiss();
    }

    protected final void setGravity(int gravity) {
        if (null != getWindow()) {
            getWindow().setGravity(gravity);
        }
    }

    @Deprecated
    protected final void setFullScreen() {
        if (null != getWindow()) {
            getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    protected final void setTransBackground() {
        if (null != getWindow()) {
            getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        }
    }

    protected final void setLayout(int width, int height) {
        if (null != getWindow()){
            getWindow().setLayout(width, height);
        }
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

