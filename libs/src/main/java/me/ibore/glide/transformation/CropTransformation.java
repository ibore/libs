package me.ibore.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.security.MessageDigest;

import me.ibore.libs.util.Utils;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-22 00:57
 * website: ibore.me
 */

public class CropTransformation implements Transformation<Bitmap> {

    private static final int VERSION = 1;
    private static final String ID = "me.ibore.libs.glide.CropTransformation." + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    public enum CropType {
        TOP,
        CENTER,
        BOTTOM
    }

    private BitmapPool mBitmapPool;
    private int mWidth;
    private int mHeight;

    private CropType mCropType = CropType.CENTER;

    public CropTransformation() {
        this(Glide.get(Utils.getApp()).getBitmapPool());
    }

    public CropTransformation(BitmapPool pool) {
        this(pool, 0, 0);
    }

    public CropTransformation(int width, int height) {
        this(Glide.get(Utils.getApp()).getBitmapPool(), width, height);
    }

    public CropTransformation(BitmapPool pool, int width, int height) {
        this(pool, width, height, CropType.CENTER);
    }

    public CropTransformation(int width, int height, CropType cropType) {
        this(Glide.get(Utils.getApp()).getBitmapPool(), width, height, cropType);
    }

    public CropTransformation(BitmapPool pool, int width, int height, CropType cropType) {
        mBitmapPool = pool;
        mWidth = width;
        mHeight = height;
        mCropType = cropType;
    }

    @Override
    public Resource<Bitmap> transform(Context context, Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();
        mWidth = mWidth == 0 ? source.getWidth() : mWidth;
        mHeight = mHeight == 0 ? source.getHeight() : mHeight;

        Bitmap.Config config =
                source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888;
        Bitmap bitmap = mBitmapPool.get(mWidth, mHeight, config);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(mWidth, mHeight, config);
        }

        float scaleX = (float) mWidth / source.getWidth();
        float scaleY = (float) mHeight / source.getHeight();
        float scale = Math.max(scaleX, scaleY);

        float scaledWidth = scale * source.getWidth();
        float scaledHeight = scale * source.getHeight();
        float left = (mWidth - scaledWidth) / 2;
        float top = getTop(scaledHeight);
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(source, null, targetRect, null);

        return BitmapResource.obtain(bitmap, mBitmapPool);
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof CropTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    private float getTop(float scaledHeight) {
        switch (mCropType) {
            case TOP:
                return 0;
            case CENTER:
                return (mHeight - scaledHeight) / 2;
            case BOTTOM:
                return mHeight - scaledHeight;
            default:
                return 0;
        }
    }
}
