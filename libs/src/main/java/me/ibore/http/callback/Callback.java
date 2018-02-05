package me.ibore.http.callback;


import me.ibore.http.HttpException;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-19 00:00
 * website: ibore.me
 */

public interface Callback<T> {

    void onSuccess(T t);

    void onError(HttpException e);

}