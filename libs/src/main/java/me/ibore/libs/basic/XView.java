package me.ibore.libs.basic;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import me.ibore.widget.LoadLayout;

public interface XView {

    XActivity getXActivity();

    //AppCompatActivity getActivity();

    Context getContext();

    LoadLayout loadLayout();

}
