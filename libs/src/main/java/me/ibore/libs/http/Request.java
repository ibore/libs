package me.ibore.libs.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import me.ibore.libs.util.ObjectUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class Request<R> {

    protected LinkedHashMap<String, String> headers;
    protected LinkedHashMap<String, List<String>> params;
    protected OkHttpClient client;

    public Request() {
        this(new LinkedHashMap<>(), new LinkedHashMap<>());
    }

    public Request(LinkedHashMap<String, String> headers, LinkedHashMap<String, List<String>> params) {
        ObjectUtils.requireNonNull(headers);
        ObjectUtils.requireNonNull(params);
        this.headers = headers;
        this.params = params;
    }

    @SuppressWarnings("unchecked")
    R client(OkHttpClient client) {
        this.client = client;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R header(String key, String value) {
        headers.put(key, value);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R param(String key, int value, boolean... isReplace) {
        return param(key, String.valueOf(value), isReplace);
    }

    @SuppressWarnings("unchecked")
    public R param(String key, double value, boolean... isReplace) {
        return param(key, String.valueOf(value), isReplace);
    }

    @SuppressWarnings("unchecked")
    public R param(String key, long value, boolean... isReplace) {
        return param(key, String.valueOf(value), isReplace);
    }

    @SuppressWarnings("unchecked")
    public R param(String key, String value, boolean... isReplace) {
        if (key != null && value != null) {
            List<String> urlValues = params.get(key);
            if (urlValues == null) {
                urlValues = new ArrayList<>();
                params.put(key, urlValues);
            }
            if (isReplace != null && isReplace.length > 0) {
                if (isReplace[0]) urlValues.clear();
            } else {
                urlValues.clear();
            }
            urlValues.add(value);
        }
        return (R) this;
    }

    public void enqueue(HttpListener listener) {
        /*client.newCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });*/
    }

    /*public void enqueue(HttpListener listener) {

    }*/

}
