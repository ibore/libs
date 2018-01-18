package me.ibore.libs.util;

import java.lang.reflect.ParameterizedType;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-18 23:54
 * website: ibore.me
 */

public class ClassUtil {

    @SuppressWarnings("unchecked")
    public static <T> T getClass(Object object, int position) {
        try {
            return ((Class<T>) ((ParameterizedType) (object.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[position])
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

}
