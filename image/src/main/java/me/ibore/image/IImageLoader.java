package me.ibore.image;

import android.app.Activity;
import android.widget.ImageView;

import java.io.Serializable;

public interface IImageLoader extends Serializable {

    void displayImage(Activity activity, String path, ImageView imageView, int width, int height);

    void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height);

    void clearMemoryCache();
}
