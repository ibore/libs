package me.ibore.libs.util;

import java.lang.reflect.ParameterizedType;

import me.ibore.libs.BuildConfig;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-18 23:54
 * website: ibore.me
 */

public class ClassUtils {

    @SuppressWarnings("unchecked")
    public static <T> T getClass(Object object, int position) {
        try {
            return ((Class<T>) ((ParameterizedType) (object.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[position])
                    .newInstance();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) e.printStackTrace();
        }
        return null;
    }

}
