package me.ibore.libs.util;
/**
 * Created by Administrator on 2018/1/19.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * <pre>
 * description:
 * author: Ibore Xie
 * date: 2018/1/19 16:00
 * website: ibore.me
 * </pre>
 */
public final class HtmlUtils {

    public static Spanned fromHtml(TextView textView, String source) {
        return fromHtml(textView, source, null);
    }

    public static Spanned fromHtml(TextView textView, String source, Html.TagHandler tagHandler) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }
        return Html.fromHtml(source, new URLImageGetter(textView), tagHandler);
    }

    public static class URLImageGetter implements Html.ImageGetter {

        private TextView textView;
        public URLImageGetter(TextView textView) {
            this.textView = textView;
        }

        @Override
        public Drawable getDrawable(final String source) {
            final URLDrawable urlDrawable = new URLDrawable();
            Glide.with(textView.getContext()).asBitmap()
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .load(source)
                    .into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    try {
                        Drawable drawable = new BitmapDrawable(bitmap);
                        drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        urlDrawable.setDrawable(drawable);
                        urlDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        textView.invalidate();
                        textView.setText(textView.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return urlDrawable;
        }
    }

    public static class URLDrawable extends BitmapDrawable {
        private Drawable drawable;
        @Override
        public void draw(Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }
        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }


}
