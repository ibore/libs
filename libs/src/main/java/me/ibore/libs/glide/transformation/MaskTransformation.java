package me.ibore.libs.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.security.MessageDigest;

import me.ibore.libs.glide.internal.Utils;

/**
 * description: 遮盖效果
 * author: Ibore Xie
 * date: 2018-01-21 22:05
 * website: ibore.me
 */

public class MaskTransformation implements Transformation<Bitmap> {

    private static final int VERSION = 1;
    private static final String ID = "me.ibore.libs.glide.MaskTransformation." + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private static Paint sMaskingPaint = new Paint();
    private BitmapPool mBitmapPool;
    private int mMaskId;

    static {
        sMaskingPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    /**
     * @param maskId If you change the mask file, please also rename the mask file, or Glide will get
     * the cache with the old mask. Because getId() return the same values if using the
     * same make file name. If you have a good idea please tell us, thanks.
     */
    public MaskTransformation(int maskId) {
        this(Glide.get(me.ibore.libs.util.Utils.getApp()).getBitmapPool(), maskId);
    }

    public MaskTransformation(BitmapPool pool, int maskId) {
        mBitmapPool = pool;
        mMaskId = maskId;
    }

    @Override
    public Resource<Bitmap> transform(Context context, Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();

        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap result = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Drawable mask = Utils.getMaskDrawable(context, mMaskId);

        Canvas canvas = new Canvas(result);
        mask.setBounds(0, 0, width, height);
        mask.draw(canvas);
        canvas.drawBitmap(source, 0, 0, sMaskingPaint);

        return BitmapResource.obtain(result, mBitmapPool);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MaskTransformation;
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

