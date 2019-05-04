package me.ibore.libs.glide.transformation;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.security.MessageDigest;

import androidx.annotation.NonNull;

public class MaskTransformation extends BitmapTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "jp.wasabeef.glide.transformations.MaskTransformation." + VERSION;

    private static Paint paint = new Paint();
    private int maskId;

    static {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    /**
     * @param maskId If you change the mask file, please also rename the mask file, or Glide will get
     *               the cache with the old mask. Because key() return the same values if using the
     *               same make file name. If you have a good idea please tell us, thanks.
     */
    public MaskTransformation(int maskId) {
        this.maskId = maskId;
    }

    @Override
    protected Bitmap transform(@NonNull Context context, @NonNull BitmapPool pool,
                               @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();

        Bitmap bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setHasAlpha(true);

        Drawable mask = me.ibore.glide.internal.Utils.getMaskDrawable(context.getApplicationContext(), maskId);

        Canvas canvas = new Canvas(bitmap);
        mask.setBounds(0, 0, width, height);
        mask.draw(canvas);
        canvas.drawBitmap(toTransform, 0, 0, paint);

        return bitmap;
    }

    @Override
    public String toString() {
        return "MaskTransformation(maskId=" + maskId + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MaskTransformation &&
                ((MaskTransformation) o).maskId == maskId;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + maskId * 10;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + maskId).getBytes(CHARSET));
    }
}
