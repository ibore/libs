package me.ibore.libs.base;

import android.app.Activity;
import android.content.Context;

import me.ibore.widget.LoadLayout;

public interface MvpView {

    Activity getActivity();

    Context getContext();

    LoadLayout loadLayout();

}
