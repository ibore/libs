package me.ibore.libs.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import me.ibore.libs.rxbus.RxBus;
import me.ibore.libs.rxbus.Subscribe;
import me.ibore.libs.rxbus.ThreadMode;
import me.ibore.libs.util.LogUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxBus.get().register(this);

        findViewById(R.id.test).setOnClickListener(v -> {
                    RxBus.get().send(new Test("1111111111"));
                }
        );
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void test(Test test) {
        LogUtils.d("0000000000000");
        //ToastUtils.showShort("测试");
        //RxBus.get().send(2);
        /*RxBus.get().send(new Test("测试2"));
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);*/
    }

    @Override
    protected void onDestroy() {
        RxBus.get().unRegister(this);
        super.onDestroy();
    }
}
