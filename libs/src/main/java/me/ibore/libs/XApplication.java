package me.ibore.libs;

import android.app.Application;
import android.content.Context;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-19 00:04
 * website: ibore.me
 */

public class XApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

}
