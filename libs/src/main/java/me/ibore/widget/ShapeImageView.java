package me.ibore.widget;
/**
 * Created by Administrator on 2018/1/19.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.gif.GifDrawable;

import me.ibore.libs.R;
import me.ibore.libs.util.SizeUtils;

/**
 * <pre>
 * description:
 * author: Ibore Xie
 * date: 2018/1/19 17:15
 * website: ibore.me
 * </pre>
 */
public class ShapeImageView extends android.support.v7.widget.AppCompatImageView {

    private static final int DEFAULT_BORDER_COLOR = Color.GRAY;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLOR_DRAWABLE_DIMEN = 2;

    private boolean mIsPressed = false;
    private boolean mIsCircle = false;

    private int mBorderWidth;
    private int mBorderColor;

    private int mPressedBorderWidth;
    private int mPressedBorderColor;
    private int mPressedMaskColor;
    private boolean mPressedModeEnabled = true;

    private int mCornerRadius;

    private Paint mBitmapPaint;
    private Paint mBorderPaint;
    private ColorFilter mColorFilter;
    private ColorFilter mPressedColorFilter;
    private BitmapShader mBitmapShader;
    private boolean mNeedResetShader = false;

    private RectF mRectF = new RectF();

    private Bitmap mBitmap;

    private Matrix mMatrix;
    private int mWidth;
    private int mHeight;

    public ShapeImageView(Context context) {
        this(context, null);
    }

    public ShapeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("CustomViewStyleable")
    public ShapeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mMatrix = new Matrix();

        setScaleType(ImageView.ScaleType.CENTER_CROP);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ShapeImageView, defStyleAttr, 0);

        mBorderWidth = array.getDimensionPixelSize(R.styleable.ShapeImageView_siv_border_width, 0);
        mBorderColor = array.getColor(R.styleable.ShapeImageView_siv_border_color, DEFAULT_BORDER_COLOR);
        mPressedBorderWidth = array.getDimensionPixelSize(R.styleable.ShapeImageView_siv_pressed_border_width, mBorderWidth);
        mPressedBorderColor = array.getColor(R.styleable.ShapeImageView_siv_pressed_border_color, mBorderColor);
        mPressedMaskColor = array.getColor(R.styleable.ShapeImageView_siv_pressed_mask_color, Color.TRANSPARENT);
        if (mPressedMaskColor != Color.TRANSPARENT) {
            mPressedColorFilter = new PorterDuffColorFilter(mPressedMaskColor, PorterDuff.Mode.DARKEN);
        }

        mPressedModeEnabled = array.getBoolean(R.styleable.ShapeImageView_siv_pressed_mode_enabled, true);
        mIsCircle = array.getBoolean(R.styleable.ShapeImageView_siv_is_circle, false);
        mCornerRadius = array.getDimensionPixelSize(R.styleable.ShapeImageView_siv_corner_radius, 0);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth(), height = getMeasuredHeight();
        if (mIsCircle) {
            int size = Math.min(width, height);
            setMeasuredDimension(size, size);
        } else {
            int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
            if (mBitmap == null) {
                return;
            }
            if (widthMode == View.MeasureSpec.AT_MOST || widthMode == View.MeasureSpec.UNSPECIFIED ||
                    heightMode == View.MeasureSpec.AT_MOST || heightMode == View.MeasureSpec.UNSPECIFIED) {
                float bmWidth = mBitmap.getWidth(), bmHeight = mBitmap.getHeight();
                float scaleX = width / bmWidth, scaleY = height / bmHeight;
                if (scaleX == scaleY) {
                    return;
                }
                if (scaleX < scaleY) {
                    setMeasuredDimension(width, (int) (bmHeight * scaleX));
                } else {
                    setMeasuredDimension((int) (bmWidth * scaleY), height);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth(), height = getHeight();
        if (width <= 0 || height <= 0 || mBitmap == null || mBitmapShader == null) {
            return;
        }

        if (mWidth != width || mHeight != height || mNeedResetShader) {
            mWidth = width;
            mHeight = height;
            updateBitmapShader();
        }

        mBorderPaint.setColor(mIsPressed ? mPressedBorderColor : mBorderColor);
        mBitmapPaint.setColorFilter(mIsPressed ? mPressedColorFilter : mColorFilter);
        int borderWidth = mIsPressed ? mPressedBorderWidth : mBorderWidth;
        mBorderPaint.setStrokeWidth(borderWidth);
        final float halfBorderWidth = borderWidth * 1.0f / 2;

        if (mIsCircle) {
            int radius = getWidth() / 2;
            canvas.drawCircle(radius, radius, radius, mBitmapPaint);
            if (borderWidth > 0) {
                canvas.drawCircle(radius, radius, radius - halfBorderWidth, mBorderPaint);
            }
        } else {
            mRectF.left = halfBorderWidth;
            mRectF.top = halfBorderWidth;
            mRectF.right = width - halfBorderWidth;
            mRectF.bottom = height - halfBorderWidth;
            canvas.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mBitmapPaint);
            if (borderWidth > 0) {
                canvas.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mBorderPaint);
            }
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setupBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        setupBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        setupBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setupBitmap();
    }

    public void setupBitmap() {
        Bitmap bitmap = getBitmap();
        if (bitmap == mBitmap) {
            return;
        }

        mBitmap = getBitmap();
        if (mBitmap == null) {
            mBitmapShader = null;
            invalidate();
            return;
        }

        mNeedResetShader = true;
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        if (mBitmapPaint == null) {
            mBitmapPaint = new Paint();
            mBitmapPaint.setAntiAlias(true);
        }
        mBitmapPaint.setShader(mBitmapShader);
        requestLayout();
        invalidate();
    }

    private Bitmap getBitmap() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof GifDrawable) {
            GifDrawable gifDrawable = (GifDrawable) drawable;
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMEN, COLOR_DRAWABLE_DIMEN, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateBitmapShader() {
        mMatrix.reset();
        mNeedResetShader = false;
        if (mBitmapShader == null || mBitmap == null) {
            return;
        }
        final float bmWidth = mBitmap.getWidth();
        final float bmHeight = mBitmap.getHeight();
        final float scaleX = mWidth / bmWidth;
        final float scaleY = mHeight / bmHeight;
        final float scale = Math.max(scaleX, scaleY);
        mMatrix.setScale(scale, scale);
        mMatrix.postTranslate(-(scale * bmWidth - mWidth) / 2, -(scale * bmHeight - mHeight) / 2);
        mBitmapShader.setLocalMatrix(mMatrix);
        mBitmapPaint.setShader(mBitmapShader);
    }

    public void setBorderWidth(int borderWidth) {
        if (mBorderWidth != borderWidth) {
            mBorderWidth =  SizeUtils.dp2px(borderWidth);
            invalidate();
        }
    }

    public void setBorderColor(@ColorInt int borderColor) {
        if (mBorderColor != borderColor) {
            mBorderColor = borderColor;
            invalidate();
        }
    }

    public void setCornerRadius(int cornerRadius) {
        if (mCornerRadius != cornerRadius) {
            mCornerRadius =  SizeUtils.dp2px(cornerRadius);
            if (!mIsCircle) {
                invalidate();
            }
        }
    }

    public void setPressedBorderColor(@ColorInt int selectedBorderColor) {
        if (mPressedBorderColor != selectedBorderColor) {
            mPressedBorderColor = selectedBorderColor;
            if (mIsPressed) {
                invalidate();
            }
        }
    }

    public void setPressedBorderWidth(int selectedBorderWidth) {
        if (mPressedBorderWidth != selectedBorderWidth) {
            mPressedBorderWidth = SizeUtils.dp2px(selectedBorderWidth);
            if (mIsPressed) {
                invalidate();
            }
        }
    }

    public void setPressedMaskColor(@ColorInt int selectedMaskColor) {
        if (mPressedMaskColor != selectedMaskColor) {
            mPressedMaskColor = selectedMaskColor;
            if (mPressedMaskColor != Color.TRANSPARENT) {
                mPressedColorFilter = new PorterDuffColorFilter(mPressedMaskColor, PorterDuff.Mode.DARKEN);
            } else {
                mPressedColorFilter = null;
            }
            if (mIsPressed) {
                invalidate();
            }
        }
        mPressedMaskColor = selectedMaskColor;
    }


    public void setCircle(boolean isCircle) {
        if (mIsCircle != isCircle) {
            mIsCircle = isCircle;
            requestLayout();
            invalidate();
        }
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public int getCornerRadius() {
        return mCornerRadius;
    }

    public int getPressedBorderColor() {
        return mPressedBorderColor;
    }

    public int getPressedBorderWidth() {
        return mPressedBorderWidth;
    }

    public int getPressedMaskColor() {
        return mPressedMaskColor;
    }

    public boolean isCircle() {
        return mIsCircle;
    }

    public boolean isPressed() {
        return mIsPressed;
    }

    public void setPressed(boolean isPressed) {
        if (mIsPressed != isPressed) {
            mIsPressed = isPressed;
            invalidate();
        }
    }

    public void setPressedModeEnabled(boolean pressedModeEnabled) {
        mPressedModeEnabled = pressedModeEnabled;
    }

    public boolean isPressedModeEnabled() {
        return mPressedModeEnabled;
    }

    public void setPressedColorFilter(ColorFilter cf) {
        if (mPressedColorFilter == cf) {
            return;
        }
        mPressedColorFilter = cf;
        if (mIsPressed) {
            invalidate();
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (mColorFilter == cf) {
            return;
        }
        mColorFilter = cf;
        if (!mIsPressed) {
            invalidate();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!this.isClickable()) {
            this.setPressed(false);
            return super.onTouchEvent(event);
        }

        if (!isPressedModeEnabled()) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.setPressed(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_SCROLL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
                this.setPressed(false);
                break;
        }
        return super.onTouchEvent(event);
    }
}
