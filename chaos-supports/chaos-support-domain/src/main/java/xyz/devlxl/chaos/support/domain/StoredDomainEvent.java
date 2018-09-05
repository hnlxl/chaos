package xyz.devlxl.chaos.support.domain;

import java.util.Date;

/**
 * The stored domain event
 * 
 * @author Liu Xiaolei
 * @date 2018/09/01
 */
public interface StoredDomainEvent {
    /**
     * Returns the ID of this event.
     * <p>
     * The consumers ignores the repeated sent mainly based on this value.
     * 
     * @return ID of this event
     */
    Long eventId();

    /**
     * Returns the body of this event that has been serialized into JSON.
     * <p>
     * The consumers rebuilds the event object mainly based on this value.
     * 
     * @return JSON
     */
    String eventBody();

    /**
     * Returns the time when the event occurred.
     * 
     * @return the time when the event occurred.
     */
    Date occurredOn();

    /**
     * Returns the class name of this event.
     * <p>
     * This value can help consumers to rebuild objects and quickly distribute events to handlers
     * 
     * @return class name
     */
    String className();
}
