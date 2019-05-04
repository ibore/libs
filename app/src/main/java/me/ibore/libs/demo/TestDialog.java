package me.ibore.libs.demo;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import me.ibore.libs.basic.XDialog;

public class TestDialog extends XDialog {

    @BindView(R.id.close)
    Button close;

    public TestDialog(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_test;
    }

    @Override
    protected void onBindView(Bundle savedInstanceState) {
        close.setOnClickListener(v -> dismiss());
    }
}
