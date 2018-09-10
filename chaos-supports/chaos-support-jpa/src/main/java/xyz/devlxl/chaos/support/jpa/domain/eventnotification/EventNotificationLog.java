package xyz.devlxl.chaos.support.jpa.domain.eventnotification;

import java.util.List;
import java.util.Optional;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A log of notification of domain event. It may be current or archived.
 * 
 * @author Liu Xiaolei
 * @date 2018/09/06
 */
@Data
@Accessors(chain = true)
public class EventNotificationLog {
    private String self;
    private Optional<String> next;
    private Optional<String> previous;
    private List<EventNotification> notifications;
    private Boolean archived;
}
