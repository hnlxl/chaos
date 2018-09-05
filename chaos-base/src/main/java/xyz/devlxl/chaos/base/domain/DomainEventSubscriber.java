package xyz.devlxl.chaos.base.domain;

/**
 * @author Liu Xiaolei
 * @date 2018/07/21
 */
@Deprecated
public interface DomainEventSubscriber<T extends DomainEvent> {

    /**
     * 监听到事件时的处理程序
     *
     * @param event 领域事件
     */
    void handle(T event);
}
