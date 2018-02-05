package me.ibore.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import me.ibore.libs.*;
import me.ibore.libs.R;
import me.ibore.widget.shadow.ViewShadow;

/**
 * Created by ibore on 18-2-6.
 */

public class ShadowView extends LinearLayout {

    public ShadowView(Context context) {
        this(context, null);
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, defStyleAttr);
    }

    private void initView(AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowView, defStyleAttr, 0);
        int shadowElevation = ta.getDimensionPixelSize(R.styleable.ShadowView_shadowElevation, 0);
        int shadowColor = ta.getColor(R.styleable.ShadowView_shadowColor, 0);
        ViewShadow.setElevation(this, shadowElevation, shadowColor);
    }
}
