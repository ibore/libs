package me.ibore.libs.demo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import me.ibore.libs.rxbus.RxBus;
import me.ibore.libs.rxbus.Subscribe;
import me.ibore.libs.rxbus.ThreadMode;
import me.ibore.libs.util.LogUtils;
import me.ibore.libs.util.ToastUtils;

public class SecondActivity extends AppCompatActivity {

    TextView tv_bus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        RxBus.get().register(this);
        tv_bus = findViewById(R.id.tv_bus);
    }

    @Subscribe(code = 2, threadMode = ThreadMode.MAIN)
    public void test(Test test) {
        LogUtils.d("22222222222222222");
        ToastUtils.showShort(test.getTest());
        tv_bus.setText("新消息");
    }

    @Override
    protected void onDestroy() {
        RxBus.get().unRegister(this);
        super.onDestroy();
    }
}
