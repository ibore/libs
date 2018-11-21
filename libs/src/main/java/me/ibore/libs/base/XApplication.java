package me.ibore.libs.base;

import android.app.Application;

import me.ibore.libs.util.Utils;
import me.ibore.widget.UIUtils;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-19 00:04
 * website: ibore.me
 */

public class XApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        UIUtils.init(this);
    }

}
