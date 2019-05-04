package me.ibore.libs.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;

import java.io.File;

import me.ibore.libs.basic.XActivity;
import me.ibore.libs.demo.fm.HomeFragment;
import me.ibore.libs.http.HttpObserver;
import me.ibore.libs.http.XHttp;
import me.ibore.libs.util.BarUtils;
import me.ibore.libs.util.LogUtils;

public class MainActivity extends XActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState) {
        BarUtils.addMarginTopEqualStatusBarHeight(getWindow());
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.onDestroy();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, new HomeFragment())
                .commit();
    }

    @Override
    protected void onBindData() {

        addDisposable(XHttp.download(
                "http://shouji.360tpcdn.com/190429/6555c2813e90cb0e18a8cd418852080a/com.qihoo.appstore_300080086.apk",
                XHttp.getClient(), getCacheDir(), progress -> {
                    LogUtils.d(progress.percent());
                }),
                new HttpObserver<File>() {
                    @Override
                    public void onSuccess(File file) {
                        LogUtils.d(file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                })
        ;
    }


}
