/*
 * Copyright 2016 yinglan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.ibore.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import me.ibore.libs.R;

public class ShadowView extends RelativeLayout {

    private int shadowWidth = 0;
    private int shadowRound = 0;
    private int shadowColor = -147483648;
    private boolean mInvalidat;

    public ShadowView(Context context) {
        this(context, null);
    }

    public ShadowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        setGravity(Gravity.CENTER);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        int imageresource = -1;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShadowView);
            imageresource = a.getResourceId(R.styleable.ShadowView_shadowSrc, -1);
            shadowRound = a.getDimensionPixelSize(R.styleable.ShadowView_shadowRound, 0);
            shadowWidth = a.getDimensionPixelSize(R.styleable.ShadowView_shadowWidth, 0);
            shadowColor = a.getColor(R.styleable.ShadowView_shadowColor, Color.parseColor("#8D8D8D"));
        }
        setPadding(shadowWidth, shadowWidth, shadowWidth, shadowWidth);
        ShapeImageView roundImageView = new ShapeImageView(context);
        roundImageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        roundImageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        if (imageresource != -1) {
//            roundImageView.setImageResource(imageresource);
//        }
        if (this.shadowColor == Color.parseColor("#8D8D8D")) {
            this.shadowColor = -147483648;
        }
        addView(roundImageView);
        mInvalidat = true;

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        if (mInvalidat) {
            mInvalidat = false;

            Paint shadowPaint = new Paint();
            shadowPaint.setColor(Color.WHITE);
            shadowPaint.setStyle(Paint.Style.FILL);
            shadowPaint.setAntiAlias(true);

            Bitmap bitmap;
            int rgb;

            if (getBackground() instanceof ColorDrawable) {
                rgb = ((ColorDrawable) getBackground()).getColor();
                shadowPaint.setShadowLayer(40, 0, 28, getDarkerColor(rgb));
            } else if (getBackground() instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable)getBackground()).getBitmap();
                Palette.Swatch mSwatch = Palette.from(bitmap).generate().getDominantSwatch();

                if (null != mSwatch) {
                    rgb = mSwatch.getRgb();
                } else {
                    rgb = Color.parseColor("#8D8D8D");
                }

                shadowPaint.setShadowLayer(shadowRound, 0, shadowWidth, getDarkerColor(rgb));
                Bitmap bitmapT = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() / 4 * 3,
                        bitmap.getWidth(), bitmap.getHeight() / 4);

                if (null != Palette.from(bitmapT).generate().getDominantSwatch()) {
                    rgb = Palette.from(bitmapT).generate().getDominantSwatch().getRgb();
                    shadowPaint.setShadowLayer(shadowRound, 0, shadowWidth, rgb);
                }
            } else {
                rgb = Color.parseColor("#8D8D8D");
                shadowPaint.setShadowLayer(shadowRound, 0, shadowWidth, getDarkerColor(rgb));
            }

            if (this.shadowColor != -147483648) {
                shadowPaint.setShadowLayer(shadowRound, 0, shadowWidth, this.shadowColor);
            }

            RectF rectF = new RectF(shadowWidth / 2, shadowWidth / 2, getWidth() - shadowWidth / 2, getHeight() - shadowWidth);

            canvas.drawRoundRect(rectF, shadowWidth, shadowWidth, shadowPaint);

            canvas.save();
        }
        super.dispatchDraw(canvas);
    }


    public int getDarkerColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[1] = hsv[1] + 0.1f;
        hsv[2] = hsv[2] - 0.1f;
        int darkerColor = Color.HSVToColor(hsv);
        return darkerColor;
    }
}
