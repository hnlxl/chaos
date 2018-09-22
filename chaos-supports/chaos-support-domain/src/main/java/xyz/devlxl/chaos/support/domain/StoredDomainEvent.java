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
     * The services related to event notifications, will use this value to issue or deliver a notification in order.
     * 
     * @return ID of this event
     */
    Long eventId();

    /**
     * Returns the body of this event that has been serialized into JSON.
     * <p>
     * The rebuilding of the event object mainly based on this value.
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
     * This value can help rebuilding of the event object or quickly distribute events to handlers
     * 
     * @return class name
     */
    String className();
}
