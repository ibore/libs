package me.ibore.libs.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.ibore.libs.rxbus.RxBus;
import me.ibore.libs.rxbus.Subscribe;
import me.ibore.libs.rxbus.ThreadMode;
import me.ibore.libs.util.ToastUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxBus.get().register(this);

        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RxBus.get().send(1);
                new TestDialog(MainActivity.this).show();
            }
        });
    }


    @Subscribe(code = 1, threadMode = ThreadMode.NEW_THREAD)
    public void test() {
        ToastUtils.showShort("测试");
    }
}
