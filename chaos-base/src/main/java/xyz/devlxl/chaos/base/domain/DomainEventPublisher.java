package xyz.devlxl.chaos.base.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Liu Xiaolei
 * @date 2018/07/21
 */
public class DomainEventPublisher {
    private ConcurrentHashMap<Class<? extends DomainEvent>,
        List<DomainEventSubscriber<? extends DomainEvent>>> subscriberMap
            = new ConcurrentHashMap<>();

    /**
     * 为某一类型的领域事件注册订阅者
     * 
     * @param eventClazz 领域事件的类型
     * @param subscriber 领域事件的订阅者
     */
    public synchronized <T extends DomainEvent> void register(Class<T> eventClazz,
        DomainEventSubscriber<T> subscriber) {
        List<DomainEventSubscriber<? extends DomainEvent>> subscribers = subscriberMap.get(eventClazz);
        if (subscribers == null) {
            subscribers = new ArrayList<>();
        }
        subscribers.add(subscriber);
        subscriberMap.put(eventClazz, subscribers);
    }

    /**
     * 发布领域事件
     * <p>
     * 目前，订阅者的的处理是同步的。但若要改成异步，也只需要修改此处，发布方和订阅方最大的修改只是发布方改一下发布方式，它们的内部业务逻辑不会有任何改动
     * 
     * @param event 领域事件，不能为空
     */
    @SuppressWarnings("unchecked")
    public <T extends DomainEvent> void publish(final T event) {
        if (event == null) {
            throw new IllegalArgumentException("domain event is null");
        }
        List<DomainEventSubscriber<? extends DomainEvent>> subscribers = subscriberMap.get(event.getClass());
        if (subscribers != null) {
            for (@SuppressWarnings("rawtypes")
            DomainEventSubscriber subscriber : subscribers) {
                subscriber.handle(event);
            }
        }
    }
}
