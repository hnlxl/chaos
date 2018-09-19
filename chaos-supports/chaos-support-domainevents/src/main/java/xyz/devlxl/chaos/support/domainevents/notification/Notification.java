package xyz.devlxl.chaos.support.domainevents.notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.devlxl.chaos.support.domain.StoredDomainEvent;

/**
 * a notification of domain event
 * 
 * @author Liu Xiaolei
 * @date 2018/09/06
 */
@Data
@Accessors(chain = true)
public class Notification {
    /**
     * The event's body that has been serialized into JSON
     * 
     * @see StoredDomainEvent#eventBody()
     */
    private String eventBody;
    /**
     * The time when the event occurred
     * 
     * @see StoredDomainEvent#occurredOn()
     */
    private Date occurredOn;
    /**
     * The event's ID, also is notification's ID
     * 
     * @see StoredDomainEvent#eventId()
     */
    private Long eventId;
    /**
     * The event's type, equivalent to the class name of the event
     * 
     * @see StoredDomainEvent#className()
     */
    private String eventType;

    public static Notification fromStoredEvent(StoredDomainEvent storedEvent) {
        return new Notification()
            .setEventBody(storedEvent.eventBody())
            .setOccurredOn(storedEvent.occurredOn())
            .setEventId(storedEvent.eventId())
            .setEventType(storedEvent.className());
    }

    public static List<Notification> listFromStoredEvent(List<? extends StoredDomainEvent> storedEvents) {
        List<Notification> notifications = new ArrayList<>(storedEvents.size());
        for (StoredDomainEvent storedEvent : storedEvents) {
            notifications.add(fromStoredEvent(storedEvent));
        }
        return notifications;
    }
}
