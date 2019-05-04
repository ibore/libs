package me.ibore.libs.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.StringRes;
import me.ibore.libs.R;
import me.ibore.libs.basic.XDialog;

public class LoadDialog extends XDialog {

    private String tipText;

    public LoadDialog(Context context) {
        this(context, null);
    }

    public LoadDialog(Context context, @StringRes int tipTextId) {
        this(context, context.getString(tipTextId));
    }

    public LoadDialog(Context context, String tipText) {
        super(context);
        if (TextUtils.isEmpty(tipText)) {
            this.tipText = context.getString(R.string.libs_dialog_tip_text);
        } else {
            this.tipText = tipText;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.libs_dialog_load;
    }

    @Override
    protected void onBindView(Bundle savedInstanceState) {
        setTransBackground();
        setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setGravity(Gravity.CENTER);
        TextView libs_tv_tip = findViewById(R.id.libs_tv_tip);
        libs_tv_tip.setText(tipText);
    }
}
