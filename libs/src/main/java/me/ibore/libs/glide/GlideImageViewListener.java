package me.ibore.libs.glide;
/**
 * Created by Administrator on 2018/1/19.
 */

import com.bumptech.glide.load.engine.GlideException;

/**
 * <pre>
 * description:
 * author: Ibore Xie
 * date: 2018/1/19 18:14
 * website: ibore.me
 * </pre>
 */
public interface GlideImageViewListener {

    void onProgress(int percent, boolean isDone, GlideException exception);
}
