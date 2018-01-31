package me.ibore.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;

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

public class CropSquareTransformation implements Transformation<Bitmap> {

    private static final int VERSION = 1;
    private static final String ID = "me.ibore.libs.glide.CropSquareTransformation." + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private BitmapPool mBitmapPool;

    public CropSquareTransformation() {
        this(Glide.get(Utils.getApp()).getBitmapPool());
    }

    public CropSquareTransformation(BitmapPool pool) {
        this.mBitmapPool = pool;
    }

    @Override
    public Resource<Bitmap> transform(Context context, Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();
        int size = Math.min(source.getWidth(), source.getHeight());

        int width = (source.getWidth() - size) / 2;
        int height = (source.getHeight() - size) / 2;

        source = Bitmap.createBitmap(source, width, height, size, size);

        return BitmapResource.obtain(source, mBitmapPool);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CropSquareTransformation;
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

