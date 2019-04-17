package me.ibore.libs.base;

import android.content.Context;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.NonNull;

public class XBottomDialog extends BottomSheetDialog {
    public XBottomDialog(@NonNull Context context) {
        super(context);
    }

    public XBottomDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

}
