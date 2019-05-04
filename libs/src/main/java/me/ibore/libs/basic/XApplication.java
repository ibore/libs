package me.ibore.libs.basic;

import android.app.Application;
import android.content.Context;

import me.ibore.libs.util.Utils;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-19 00:04
 * website: ibore.me
 */

public class XApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }

}
