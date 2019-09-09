package me.ibore.libs.demo.fm;

import android.os.Bundle;

import androidx.core.text.HtmlCompat;

import me.ibore.libs.basic.XFragment;
import me.ibore.libs.demo.R;
import me.ibore.libs.demo.Test;
import me.ibore.libs.rxbus.RxBus;
import me.ibore.libs.util.LogUtils;

public class HomeFragment extends XFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onBindView(Bundle savedInstanceState) {
        getView().findViewById(R.id.test1).setOnClickListener(v -> {
            new XAlertDialog(getActivity())
                    .setTitle("标题")
                    .setContent(HtmlCompat.fromHtml("<a>This is a  <font color='#0000FF'> blue text</font> and this is a <a href=\"tel:10086\"><font color='red'>10086</font></a><font color='red'> red text</font> </a>", HtmlCompat.FROM_HTML_MODE_COMPACT))
                    .setLeftText()
                    .setRightText()
                    .show();
//            RxBus.get().send(11);
            //new DialogFragment().show(getFragmentManager(), DialogFragment.class.getSimpleName());
        });
        getView().findViewById(R.id.test2).setOnClickListener(v -> {
            RxBus.get().send("test2");
            //new DialogFragment().show(getFragmentManager(), DialogFragment.class.getSimpleName());
        });
        getView().findViewById(R.id.test3).setOnClickListener(v -> {
            RxBus.get().send(22, new Test("test3"));
            //new DialogFragment().show(getFragmentManager(), DialogFragment.class.getSimpleName());
        });
    }

    @Override
    protected void onBindData() {
        LogUtils.d("---------------");
    }

    @Override
    public boolean onBackPressed() {
        LogUtils.d("dddddddddddddddd");
        return super.onBackPressed();
    }
}
