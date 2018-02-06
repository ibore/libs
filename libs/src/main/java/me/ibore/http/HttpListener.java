package me.ibore.http;

import me.ibore.http.HttpException;

/**
 * Created by Administrator on 2018/2/6.
 */

public interface HttpListener<T> extends ProgressListener {

    void onSuccess(T t);

    void onError(HttpException e);

}
