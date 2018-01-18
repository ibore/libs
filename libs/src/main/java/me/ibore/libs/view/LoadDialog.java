package me.ibore.libs.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;

import me.ibore.libs.R;
import me.ibore.libs.base.XDialog;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-19 00:06
 * website: ibore.me
 */

public class LoadDialog extends XDialog {

    private String message;
    private TextView textView;

    public LoadDialog(@NonNull Context context) {
        this(context, null);
    }

    public LoadDialog(@NonNull Context context, @StringRes int resId) {
        this(context, context.getString(resId));
    }

    public LoadDialog(@NonNull Context context, String message) {
        super(context);
        if (TextUtils.isEmpty(message)) {
            this.message = context.getString(R.string.loading);
        } else {
            this.message = message;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_load;
    }

    @Override
    protected void onBindView(Bundle savedInstanceState) {
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        textView = findViewById(R.id.message);
        textView.setText(message);
    }

    public void setMessage(String message) {
        textView.setText(message);
    }

    public void setMessage(int message) {
        textView.setText(message);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if(isShowing()) cancel();
                break;
        }
        return true;
    }
}
