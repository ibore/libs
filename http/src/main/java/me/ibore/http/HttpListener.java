package me.ibore.http;

/**
 * Created by Administrator on 2018/2/6.
 */

public interface HttpListener<T> {

    void onSuccess(T t);

    void onError(HttpException e);

}
