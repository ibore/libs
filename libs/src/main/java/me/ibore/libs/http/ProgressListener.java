package me.ibore.libs.http;

import com.bumptech.glide.load.engine.GlideException;

/**
 * Created by Administrator on 2018/1/19.
 */

public interface ProgressListener {

    void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone, GlideException exception);

}
