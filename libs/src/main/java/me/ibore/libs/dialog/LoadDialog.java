package me.ibore.libs.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.StringRes;
import me.ibore.libs.R;
import me.ibore.libs.basic.XDialog;

public class LoadDialog extends XDialog {

    private String tipText;

    public LoadDialog(Context context) {
        super(context);
        this.tipText = context.getString(R.string.libs_dialog_tip_text);
    }

    public LoadDialog(Context context, @StringRes int tipTextId) {
        super(context);
        this.tipText = context.getString(tipTextId);
    }

    public LoadDialog(Context context, String tipText) {
        super(context);
        this.tipText = tipText;
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
