package me.ibore.libs.rxbus;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-18 23:55
 * website: ibore.me
 */

public class RxBus {

    private static volatile RxBus defaultInstance;

    private Map<Class, List<Disposable>> subscriptionsByEventType = new HashMap<>();

    private Map<Object, List<Class>> eventTypesBySubscriber = new HashMap<>();

    private Map<Class, List<SubscriberMethod>> subscriberMethodByEventType = new HashMap<>();

    /*stick数据*/
    private final Map<Class<?>, Object> stickyEvent = new ConcurrentHashMap<>();

    private final Subject<Object> bus;

    private RxBus() {
        this.bus = PublishSubject.create().toSerialized();
    }

    public static RxBus get() {
        RxBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param eventType 事件类型
     * @return return
     */
    private <T> Flowable<T> toFlowable(Class<T> eventType) {
        return bus.toFlowable(BackpressureStrategy.BUFFER).ofType(eventType);
    }

    /**
     * 根据传递的code和 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param code      事件code
     * @param eventType 事件类型
     */
    private <T> Flowable<T> toFlowable(final int code, final Class<T> eventType) {
        return bus.toFlowable(BackpressureStrategy.BUFFER).ofType(Message.class)
                .filter(new Predicate<Message>() {
                    @Override
                    public boolean test(Message o) throws Exception {
                        return o.getCode() == code && eventType.isInstance(o.getObject());
                    }
                }).map(new Function<Message, Object>() {
                    @Override
                    public Object apply(Message o) throws Exception {
                        return o.getObject();
                    }
                }).cast(eventType);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Flowable<T> toFlowableSticky(final Class<T> eventType) {
        synchronized (stickyEvent) {
            Flowable<T> flowable = bus.toFlowable(BackpressureStrategy.BUFFER).ofType(eventType);
            final Object event = stickyEvent.get(eventType);
            if (event != null) {
                return flowable.mergeWith(new Publisher<T>() {
                    @Override
                    public void subscribe(Subscriber<? super T> s) {
                        s.onNext(eventType.cast(event));
                    }
                });
            } else {
                return flowable;
            }
        }
    }

    /**
     * 注册
     *
     * @param subscriber 订阅者
     */
    public void register(Object subscriber) {
        Class<?> subClass = subscriber.getClass();
        Method[] methods = subClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                //获得参数类型
                Class[] parameterType = method.getParameterTypes();
                Class eventType = null;
                //参数不为空 且参数个数为1
                if (parameterType != null && parameterType.length == 1) {
                    eventType = parameterType[0];
                } else if (parameterType == null || parameterType.length == 0) {
                    eventType = BusData.class;
                }
                if (null != eventType) {
                    addEventTypeToMap(subscriber, eventType);
                    Subscribe sub = method.getAnnotation(Subscribe.class);
                    int code = sub.code();
                    ThreadMode threadMode = sub.threadMode();
                    boolean sticky = sub.sticky();
                    SubscriberMethod subscriberMethod = new SubscriberMethod(subscriber, method, eventType, code, threadMode, sticky);
                    if (isAdd(eventType, subscriberMethod)) {
                        addSubscriberToMap(eventType, subscriberMethod);
                        addSubscriber(subscriberMethod);
                    }
                }
            }
        }
    }

    /**
     * 检查是否已经添加过sub事件
     *
     * @param eventType
     * @param subscriberMethod
     * @return
     */
    private boolean isAdd(Class eventType, SubscriberMethod subscriberMethod) {
        boolean resulte = true;
        List<SubscriberMethod> subscriberMethods = subscriberMethodByEventType.get(eventType);
        if (subscriberMethods != null && subscriberMethods.size() > 0) {
            for (SubscriberMethod subscriberMethod1 : subscriberMethods) {
                if (subscriberMethod1.code == subscriberMethod.code && subscriberMethod.subscriber == subscriberMethod1.subscriber) {
                    resulte = false;
                }
                if (subscriberMethod1.eventType == subscriberMethod.eventType && subscriberMethod.subscriber == subscriberMethod1.subscriber) {
                    resulte = false;
                }
            }
        }
        return resulte;
    }


    /**
     * 将event的类型以订阅中subscriber为key保存到map里
     *
     * @param subscriber 订阅者
     * @param eventType  event类型
     */
    private void addEventTypeToMap(Object subscriber, Class eventType) {
        List<Class> eventTypes = eventTypesBySubscriber.get(subscriber);
        if (eventTypes == null) {
            eventTypes = new ArrayList<>();
            eventTypesBySubscriber.put(subscriber, eventTypes);
        }
        if (!eventTypes.contains(eventType)) {
            eventTypes.add(eventType);
        }
    }

    /**
     * 将注解方法信息以event类型为key保存到map中
     *
     * @param eventType        event类型
     * @param subscriberMethod 注解方法信息
     */
    private void addSubscriberToMap(Class eventType, SubscriberMethod subscriberMethod) {
        List<SubscriberMethod> subscriberMethods = subscriberMethodByEventType.get(eventType);
        if (subscriberMethods == null) {
            subscriberMethods = new ArrayList<>();
            subscriberMethodByEventType.put(eventType, subscriberMethods);
        }
        if (!subscriberMethods.contains(subscriberMethod)) {
            subscriberMethods.add(subscriberMethod);
        }
    }

    /**
     * 将订阅事件以event类型为key保存到map,用于取消订阅时用
     *
     * @param eventType  event类型
     * @param disposable 订阅事件
     */
    private void addSubscriptionToMap(Class eventType, Disposable disposable) {
        List<Disposable> disposables = subscriptionsByEventType.get(eventType);
        if (disposables == null) {
            disposables = new ArrayList<>();
            subscriptionsByEventType.put(eventType, disposables);
        }
        if (!disposables.contains(disposable)) {
            disposables.add(disposable);
        }
    }

    /**
     * 用RxJava添加订阅者
     *
     * @param subscriberMethod d
     */
    @SuppressWarnings("unchecked")
    private void addSubscriber(final SubscriberMethod subscriberMethod) {

        Flowable flowable;
        if (subscriberMethod.sticky) {
            flowable = toFlowableSticky(subscriberMethod.eventType);
        } else {
            if (subscriberMethod.code == -1) {
                flowable = toFlowable(subscriberMethod.eventType);
            } else {
                flowable = toFlowable(subscriberMethod.code, subscriberMethod.eventType);
            }
        }

        Disposable subscription = postToObservable(flowable, subscriberMethod)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        callEvent(subscriberMethod, o);
                    }
                });

        addSubscriptionToMap(subscriberMethod.subscriber.getClass(), subscription);
    }

    /**
     * 用于处理订阅事件在那个线程中执行
     *
     * @param observable       d
     * @param subscriberMethod d
     * @return Observable
     */
    private Flowable postToObservable(Flowable observable, SubscriberMethod subscriberMethod) {
        Scheduler scheduler;
        switch (subscriberMethod.threadMode) {
            case MAIN:
                scheduler = AndroidSchedulers.mainThread();
                break;
            case NEW_THREAD:
                scheduler = Schedulers.newThread();
                break;
            case CURRENT_THREAD:
                scheduler = Schedulers.trampoline();
                break;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscriberMethod.threadMode);
        }
        return observable.observeOn(scheduler);
    }

    /**
     * 回调到订阅者的方法中
     *
     * @param method code
     * @param object obj
     */
    private void callEvent(SubscriberMethod method, Object object) {
        Class eventClass = object.getClass();
        List<SubscriberMethod> methods = subscriberMethodByEventType.get(eventClass);
        if (methods != null && methods.size() > 0) {
            for (SubscriberMethod subscriberMethod : methods) {
                Subscribe sub = subscriberMethod.method.getAnnotation(Subscribe.class);
                int c = sub.code();
                if (c == method.code && method.subscriber.equals(subscriberMethod.subscriber) && method.method.equals(subscriberMethod.method)) {
                    subscriberMethod.invoke(object);
                }

            }
        }
    }

    /**
     * 取消注册
     *
     * @param subscriber object
     */
    public void unRegister(Object subscriber) {
        List<Class> subscribedTypes = eventTypesBySubscriber.get(subscriber);
        if (subscribedTypes != null) {
            for (Class<?> eventType : subscribedTypes) {
                unSubscribeByEventType(subscriber.getClass());
                unSubscribeMethodByEventType(subscriber, eventType);
            }
            eventTypesBySubscriber.remove(subscriber);
        }
    }

    /**
     * subscriptions unsubscribe
     *
     * @param eventType eventType
     */
    private void unSubscribeByEventType(Class eventType) {
        List<Disposable> disposables = subscriptionsByEventType.get(eventType);
        if (disposables != null) {
            Iterator<Disposable> iterator = disposables.iterator();
            while (iterator.hasNext()) {
                Disposable disposable = iterator.next();
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 移除subscriber对应的subscriberMethods
     *
     * @param subscriber subscriber
     * @param eventType  eventType
     */
    private void unSubscribeMethodByEventType(Object subscriber, Class eventType) {
        List<SubscriberMethod> subscriberMethods = subscriberMethodByEventType.get(eventType);
        if (subscriberMethods != null) {
            Iterator<SubscriberMethod> iterator = subscriberMethods.iterator();
            while (iterator.hasNext()) {
                SubscriberMethod subscriberMethod = iterator.next();
                if (subscriberMethod.subscriber.equals(subscriber)) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (stickyEvent) {
            return eventType.cast(stickyEvent.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (stickyEvent) {
            stickyEvent.clear();
        }
    }

    public void send(int code, Object o) {
        bus.onNext(new Message(code, o));
    }

    public void send(Object o) {
        bus.onNext(o);
    }

    public void sendSticky(Object o) {
        synchronized (stickyEvent) {
            stickyEvent.put(o.getClass(), o);
        }
        bus.onNext(o);
    }

    public void send(int code) {
        bus.onNext(new Message(code, new BusData()));
    }

    private class Message {
        private int code;
        private Object object;

        public Message() {
        }

        private Message(int code, Object o) {
            this.code = code;
            this.object = o;
        }

        private int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        private Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }
}