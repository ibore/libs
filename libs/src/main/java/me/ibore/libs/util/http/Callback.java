package me.ibore.libs.util.http;

/**
 * <pre>
 *     author: blankj
 *     blog  : http://blankj.com
 *     time  : 2019/02/17
 * </pre>
 */
public interface Callback {
    void onResponse(Response response);

    void onFailed(Exception e);
}
