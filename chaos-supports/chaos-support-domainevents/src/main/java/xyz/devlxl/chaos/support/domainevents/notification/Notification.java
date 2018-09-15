package xyz.devlxl.chaos.support.domainevents.notification;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.devlxl.chaos.support.domain.DomainEvent;

/**
 * a notification of domain event
 * 
 * @author Liu Xiaolei
 * @date 2018/09/06
 */
@Data
@Accessors(chain = true)
public class Notification {
    private DomainEvent event;
    private Date eventOccureedOn;
    private Long eventId;
    private String eventClassName;
}
