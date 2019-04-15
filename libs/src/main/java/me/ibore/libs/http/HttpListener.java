package me.ibore.libs.http;

/**
 * Http监听
 * @param <T>
 */
public interface HttpListener<T> {

    void onSuccess(T t);

    void onError(Exception e);
}