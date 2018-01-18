package me.ibore.libs.http;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-19 00:00
 * website: ibore.me
 */

public interface XCallback<T> {

    void onSuccess(T t);

    void onError(XHttpException e);

}