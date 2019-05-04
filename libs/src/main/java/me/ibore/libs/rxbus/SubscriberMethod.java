package me.ibore.libs.rxbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-18 23:56
 * website: ibore.me
 */
class SubscriberMethod {

    Method method;
    ThreadMode threadMode;
    Class<?> eventType;
    Object subscriber;
    public int code;

    SubscriberMethod(Object subscriber, Method method, Class<?> eventType, int code, ThreadMode threadMode) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
        this.subscriber = subscriber;
        this.code = code;
    }


    /**
     * 调用方法
     *
     * @param o 参数
     */
    void invoke(Object o) {
        try {
            Class[] parameterType = method.getParameterTypes();
            if (parameterType != null && parameterType.length == 1) {
                method.invoke(subscriber, o);
            } else if (parameterType == null || parameterType.length == 0) {
                method.invoke(subscriber);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toString() {
        return "SubscriberMethod{" +
                "method=" + method +
                ", threadMode=" + threadMode +
                ", eventType=" + eventType +
                ", subscriber=" + subscriber +
                ", code=" + code +
                '}';
    }
}
