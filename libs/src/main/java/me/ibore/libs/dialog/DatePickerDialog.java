package me.ibore.libs.dialog;

import android.content.Context;
import android.os.Bundle;

import me.ibore.libs.R;
import me.ibore.libs.basic.XDialog;

public class DatePickerDialog extends XDialog {

    public DatePickerDialog(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.libs_dialog_date_picker;
    }

    @Override
    protected void onBindView(Bundle savedInstanceState) {

    }
}
