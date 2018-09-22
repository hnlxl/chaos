package xyz.devlxl.chaos.support.domainevents.notification;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.devlxl.chaos.base.JsonSerializationException;
import xyz.devlxl.chaos.support.domain.DomainEvent;
import xyz.devlxl.chaos.support.domain.StoredDomainEvent;

/**
 * A notification of domain event
 * 
 * @author Liu Xiaolei
 * @date 2018/09/06
 */
@Data
@Accessors(chain = true)
public class Notification {
    private final static String F_N_OCCURRED_ON = "occurredOn";
    /**
     * The domain event of this notification
     * <p>
     * Note:<br>
     * a command in notification is also a domain event.
     * <p>
     * <strong>Warning: <br>
     * This field's instance, should be an instance of {@link DomainEvent}, or can be converted into it.</strong>
     * 
     * @see DomainEvent
     */
    private Object event;
    /**
     * The time when the event occurred or when the command created
     */
    private Date occurredOn;
    /**
     * The notification's ID, sometimes it equal to the stored event's ID
     */
    private Long notificationId;
    /**
     * The event or command's type name, sometimes it equal to the class name of the event
     */
    private String typeName;

    public static Notification fromStoredEvent(ObjectMapper objcetMapper, StoredDomainEvent storedEvent) {
        try {
            return new Notification()
                .setEvent(objcetMapper.readValue(storedEvent.eventBody(), Class.forName(storedEvent.className())))
                .setOccurredOn(storedEvent.occurredOn())
                .setNotificationId(storedEvent.eventId())
                .setTypeName(storedEvent.className());
        } catch (ClassNotFoundException | IOException e) {
            throw new JsonSerializationException(e);
        }
    }

    public static List<Notification> listFromStoredEvent(ObjectMapper objcetMapper,
        List<? extends StoredDomainEvent> storedEvents) {
        List<Notification> notifications = new ArrayList<>(storedEvents.size());
        for (StoredDomainEvent storedEvent : storedEvents) {
            notifications.add(fromStoredEvent(objcetMapper, storedEvent));
        }
        return notifications;
    }

    /**
     * Create a command notification from the original information of the command
     * 
     * @param type the command type
     * @param parameters the command parameters. Optional, will defaults to empty map if not be provided.
     * @param occurredOn the time when the command created. Optional, will defaults to current time if not be provided.
     * @param notificationId the notification's id . Optional, will defaults to -1 if not be provided.
     * @return
     */
    public static Notification fromCommandOriginalInfo(String type, Map<String, Object> parameters, Date occurredOn,
        Long notificationId) {
        checkNotNull(type);
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        if (occurredOn == null) {
            occurredOn = new Date();
        }
        if (notificationId == null) {
            notificationId = -1L;
        }
        parameters.put(F_N_OCCURRED_ON, occurredOn);
        return new Notification()
            .setEvent(parameters)
            .setOccurredOn(occurredOn)
            .setNotificationId(notificationId)
            .setTypeName(type);
    }
}
