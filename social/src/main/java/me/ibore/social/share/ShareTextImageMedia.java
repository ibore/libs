package me.ibore.social.share;

import android.graphics.Bitmap;

/**
 * 文字图片分享 实体类
 */
public class ShareTextImageMedia implements IShareMedia {
    private Bitmap mImage;

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap image) {
        mImage = image;
    }

    private String mText;

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
