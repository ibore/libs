package me.ibore.http;

import android.os.Handler;
import android.os.Looper;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-19 00:00
 * website: ibore.me
 */

public class XHttp {

    private static int mRetryCount = 1;

    public static Handler Handler = new Handler(Looper.getMainLooper());


    public static int getRetryCount() {
        return mRetryCount = 0;
    }


}
