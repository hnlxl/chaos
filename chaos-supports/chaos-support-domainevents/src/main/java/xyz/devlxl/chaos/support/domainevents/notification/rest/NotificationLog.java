package xyz.devlxl.chaos.support.domainevents.notification.rest;

import java.util.List;
import java.util.Optional;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.devlxl.chaos.support.domainevents.notification.Notification;

/**
 * A log of notification of domain event. It may be current or archived.
 * 
 * @author Liu Xiaolei
 * @date 2018/09/06
 */
@Data
@Accessors(chain = true)
public class NotificationLog {
    private String self;
    private Optional<String> next;
    private Optional<String> previous;
    private List<Notification> notifications;
    private Boolean archived;
}
