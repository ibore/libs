package me.ibore.libs.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import me.ibore.libs.R;
import me.ibore.libs.basic.XDialog;

public class XAlertDialog extends XDialog implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvLeft;
    private View viewLine;
    private TextView tvRight;

    private CharSequence title;
    private CharSequence content;
    private @ColorInt int titleColor = -1;
    private @ColorInt int contentColor = -1;
    private @ColorInt int contentLinkTextColor = -1;
    private int contentGravity = Gravity.CENTER;
    private boolean isShowLeft;
    private boolean isShowRight;
    private CharSequence leftText;
    private CharSequence rightText;
    private int leftColor = -1;
    private int rightColor = -1;
    private OnClickListener mLeftOnClickListener;
    private OnClickListener mRightOnClickListener;

    public XAlertDialog(Context context) {
        super(context);
    }

    public XAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.libs_dialog_alert;
    }

    @Override
    protected void onBindView(Bundle savedInstanceState) {
        setFullScreen(Gravity.CENTER);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvLeft = findViewById(R.id.tv_left);
        viewLine = findViewById(R.id.view_line);
        tvRight = findViewById(R.id.tv_right);

        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
            tvTitle.setText("");
            if (-1 != titleColor) {
                tvTitle.setTextColor(titleColor);
            }
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        if (-1 != contentColor) {
            tvContent.setTextColor(contentColor);
        }
        if (-1 != contentLinkTextColor) {
            tvContent.setLinkTextColor(contentLinkTextColor);
        }
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent.setText(content);
        tvContent.setGravity(contentGravity);
        tvLeft.setVisibility(isShowLeft ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(leftText)) {
            tvLeft.setText(leftText);
        }
        tvRight.setVisibility(isShowRight ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(rightText)) {
            tvRight.setText(rightText);
        }
        viewLine.setVisibility(isShowLeft && isShowRight ? View.VISIBLE : View.GONE);
        if (leftColor != -1) {
            tvLeft.setTextColor(leftColor);
        }
        if (rightColor != -1) {
            tvRight.setTextColor(rightColor);
        }
    }

    public XAlertDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public XAlertDialog setTitleColor(@ColorInt int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public XAlertDialog setContent(CharSequence content) {
        this.content = content;
        return this;
    }

    public XAlertDialog setContentColor(@ColorInt int contentColor) {
        this.contentColor = contentColor;
        return this;
    }

    public XAlertDialog setContentLinkTextColor(@ColorInt int contentLinkTextColor) {
        this.contentLinkTextColor = contentLinkTextColor;
        return this;
    }

    public XAlertDialog setContentGravity(int gravity) {
        this.contentGravity = gravity;
        return this;
    }

    public XAlertDialog setLeftText() {
        this.isShowLeft = true;
        return this;
    }

    public XAlertDialog setLeftText(OnClickListener onClickListener) {
        this.isShowLeft = true;
        this.mLeftOnClickListener = onClickListener;
        return this;
    }

    public XAlertDialog setLeftText(CharSequence text, OnClickListener onClickListener) {
        this.isShowLeft = true;
        this.leftText = text;
        this.mLeftOnClickListener = onClickListener;
        return this;
    }

    public XAlertDialog setLeftText(CharSequence text, @ColorInt int colorInt, OnClickListener onClickListener) {
        this.isShowLeft = true;
        this.leftText = text;
        this.leftColor = colorInt;
        this.mLeftOnClickListener = onClickListener;
        return this;
    }

    public XAlertDialog setRightText() {
        this.isShowRight = true;
        return this;
    }

    public XAlertDialog setRightText(OnClickListener onClickListener) {
        this.isShowRight = true;
        this.mRightOnClickListener = onClickListener;
        return this;
    }

    public XAlertDialog setRightText(CharSequence text, OnClickListener onClickListener) {
        this.isShowRight = true;
        this.rightText = text;
        this.mRightOnClickListener = onClickListener;
        return this;
    }

    public XAlertDialog setRightText(CharSequence text, @ColorInt int colorInt, OnClickListener onClickListener) {
        this.isShowRight = true;
        this.rightText = text;
        this.rightColor = colorInt;
        this.mRightOnClickListener = onClickListener;
        return this;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_left) {
            if (null == mLeftOnClickListener) {
                dismiss();
            } else {
                mLeftOnClickListener.onClick(this);
                dismiss();
            }
        } else if (viewId == R.id.tv_right) {
            if (null == mRightOnClickListener) {
                dismiss();
            } else {
                mRightOnClickListener.onClick(this);
                dismiss();
            }
        }
    }

    public interface OnClickListener {

        void onClick(XAlertDialog dialog);

    }
}
