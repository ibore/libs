package me.ibore.http;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by Administrator on 2018/2/7.
 */

public class BitmapInfo {

    private String url;
    private File file;
    private String fileName;
    private Bitmap bitmap;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "BitmapInfo{" +
                "url='" + url + '\'' +
                ", file=" + file +
                ", fileName='" + fileName + '\'' +
                ", bitmap=" + bitmap +
                '}';
    }
}
