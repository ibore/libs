package me.ibore.libs.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.security.MessageDigest;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-22 00:58
 * website: ibore.me
 */

public class GrayscaleTransformation implements Transformation<Bitmap> {

    private static final int VERSION = 1;
    private static final String ID = "me.ibore.libs.glide.GrayscaleTransformation." + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private BitmapPool mBitmapPool;

    public GrayscaleTransformation(Context context) {
        this(Glide.get(context).getBitmapPool());
    }

    public GrayscaleTransformation(BitmapPool pool) {
        mBitmapPool = pool;
    }

    @Override
    public Resource<Bitmap> transform(Context context, Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();

        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap.Config config =
                source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888;
        Bitmap bitmap = mBitmapPool.get(width, height, config);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, config);
        }

        Canvas canvas = new Canvas(bitmap);
        ColorMatrix saturation = new ColorMatrix();
        saturation.setSaturation(0f);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(saturation));
        canvas.drawBitmap(source, 0, 0, paint);

        return BitmapResource.obtain(bitmap, mBitmapPool);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GrayscaleTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
