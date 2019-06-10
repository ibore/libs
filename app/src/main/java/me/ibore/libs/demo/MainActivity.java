package me.ibore.libs.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;

import me.ibore.libs.basic.XActivity;
import me.ibore.libs.demo.fm.HomeFragment;
import me.ibore.libs.rxbus.Subscribe;
import me.ibore.libs.rxbus.ThreadMode;
import me.ibore.libs.util.BarUtils;
import me.ibore.libs.util.LogUtils;
import me.ibore.libs.util.ToastUtils;

public class MainActivity extends XActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState) {
        BarUtils.addMarginTopEqualStatusBarHeight(rootView);

    }

    @Override
    protected void onBindData() {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.onDestroy();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, new HomeFragment())
                .commit();


    }


    @Subscribe(code = 11, threadMode = ThreadMode.MAIN)
    public void ss() {
        ToastUtils.showShort("dddddddddd");
        LogUtils.d("wwwwwwwwwwwwwww");
    }

    @Subscribe(code = 22, threadMode = ThreadMode.MAIN)
    public void ss(Test test) {
        ToastUtils.showShort("dfffffffffffffffff");
        LogUtils.d("fffffffffffffffffff");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sssss(String test) {
        ToastUtils.showShort(test);
        LogUtils.d(test);
    }
}
