package xyz.devlxl.chaos.support.domain;

import java.util.Date;

/**
 * @author Liu Xiaolei
 * @date 2018/08/28
 */
public interface DomainEvent {
    /**
     * The time when the event occurred. It is for reference only and is not used as an event identifier, But it is
     * still one of the identification fields.
     * 
     * @return
     */
    public Date occurredOn();

    /**
     * Indicates whether this event is equal to another one when the time of occurrence is excluded. This method is
     * usually used during unit testing and is rarely used under normal circumstances.
     * 
     * @param event
     * @return
     */
    public default boolean equalsExcludedOccurTime(DomainEvent other) {
        return false;
    }
}
