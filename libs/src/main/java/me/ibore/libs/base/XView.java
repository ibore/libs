package me.ibore.libs.base;

import android.app.Activity;
import android.content.Context;

import me.ibore.widget.LoadLayout;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-18 23:51
 * website: ibore.me
 */

public interface XView {

    Activity getActivity();

    Context getContext();

    LoadLayout loadLayout();
}
