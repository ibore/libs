package me.ibore.libs.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-18 23:59
 * website: ibore.me
 */

public abstract class XLayout extends FrameLayout {

    public XLayout(@NonNull Context context) {
        this(context, null);
    }

    public XLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onBindView();
    }

    protected abstract void onBindView();

}