package me.ibore.libs.http;

import android.text.TextUtils;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-19 00:00
 * website: ibore.me
 */

public class XHttpException extends Exception {

    public static final int NetworkNotConnected = 1000;
    public static final int SocketTimeout = 1001;

    private int mCode;

    public XHttpException(int code, String detailMessage) {
        super(TextUtils.isEmpty(detailMessage) ? "" : detailMessage);
        this.mCode = code;
    }

    /** 获取错误状态码 */
    public int getCode() {
        return mCode;
    }

    @Override public String toString() {
        return "HttpException{" +
                "code=" + mCode +
                ", detailMessage='" + getMessage() + '\'' +
                '}';
    }

}

