package me.ibore.libs.util;

public class HttpUtils {


    public static class Request {

    }

    public interface Callback {

        void onSuccess();

        void onError(Throwable t);
    }

}
