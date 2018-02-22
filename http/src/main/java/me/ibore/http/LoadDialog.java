package me.ibore.http;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/2/22.
 */

public class LoadDialog extends Dialog {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_load);
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
