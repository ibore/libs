package me.ibore.libs.glide;
/**
 * Created by Administrator on 2018/1/19.
 */

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import me.ibore.http.ProgressManager;

/**
 * <pre>
 * description:
 * author: Ibore Xie
 * date: 2018/1/19 18:09
 * website: ibore.me
 * </pre>
 */
//@GlideModule
//public class XAppGlideModule extends AppGlideModule {
//    @Override
//    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
//        super.registerComponents(context, glide, registry);
//        registry.replace(GlideUrl.class, InputStream.class,
//                new OkHttpUrlLoader.Factory(ProgressManager.getOkHttpClient()));
//    }
//}
