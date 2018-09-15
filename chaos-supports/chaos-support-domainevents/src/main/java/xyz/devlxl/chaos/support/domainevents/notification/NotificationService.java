package xyz.devlxl.chaos.support.domainevents.notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Setter;
import xyz.devlxl.chaos.support.domain.DomainEvent;
import xyz.devlxl.chaos.support.domainevents.store.JpaStoredDomainEvent;
import xyz.devlxl.chaos.support.domainevents.store.JpaStoredDomainEventRepository;
import xyz.devlxl.chaos.support.domainevents.store.JsonSerializationException;

/**
 * The application service of the stored domain events's notification based on REST
 * 
 * @author Liu Xiaolei
 * @date 2018/09/06
 */
@Service
public class NotificationService {
    public static final int LOG_NOTIFICATION_COUNT = 20;

    @Setter(onMethod_ = @Autowired)
    private JpaStoredDomainEventRepository jpaStoredDomainEventRepository;

    @Setter(onMethod_ = {@Qualifier("objectMapperOfDomainEventsSupport"), @Autowired})
    private ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public NotificationLog currentNotificationLog() {
        return findNotificationLog(calculateCurrentNotificationLogId());
    }

    @Transactional(readOnly = true)
    public NotificationLog notificationLog(String notificationLogId) {
        return findNotificationLog(NotificationLogId.from(notificationLogId));
    }

    protected NotificationLogId calculateCurrentNotificationLogId() {
        // Using MAX instead of COUNT，so we don't have to ask for the ID to start from one and be continuous.
        long max = jpaStoredDomainEventRepository.maxId();

        long remainder = max % LOG_NOTIFICATION_COUNT;
        if (remainder == 0) {
            remainder = LOG_NOTIFICATION_COUNT;
        }

        long low = max - remainder + 1;
        // 虽然当前有可能不存在一整套的通知，但是日志的id应该是暂新的
        long high = low + LOG_NOTIFICATION_COUNT - 1;

        return new NotificationLogId(low, high);
    }

    protected NotificationLog findNotificationLog(NotificationLogId notificationLogId) {
        List<JpaStoredDomainEvent> storedEvents
            = jpaStoredDomainEventRepository.findAllByEventIdBetween(notificationLogId.low(),
                notificationLogId.high(), Sort.by(Order.asc("eventId")));
        List<Notification> notifications = notificationsFrom(storedEvents);

        long max = jpaStoredDomainEventRepository.maxId();
        boolean archived = notificationLogId.high() < max;

        Optional<String> next = Optional.empty();
        if (archived) {
            next = Optional.of(notificationLogId.next().encoded());
        }

        Optional<String> previous = notificationLogId.previous()
            .map((previousId) -> {
                return previousId.encoded();
            });

        NotificationLog log = new NotificationLog()
            .setSelf(notificationLogId.encoded())
            .setNext(next)
            .setPrevious(previous)
            .setNotifications(notifications)
            .setArchived(archived);

        return log;
    }

    protected List<Notification> notificationsFrom(List<JpaStoredDomainEvent> storedEvents) {
        List<Notification> notifications = new ArrayList<>(storedEvents.size());
        for (JpaStoredDomainEvent storedEvent : storedEvents) {
            DomainEvent domainEvent = null;
            try {
                domainEvent = (DomainEvent)objectMapper.readValue(
                    storedEvent.eventBody(),
                    Class.forName(storedEvent.className()));
            } catch (IOException | ClassNotFoundException e) {
                throw new JsonSerializationException(e);
            }

            Notification notification = new Notification()
                .setEvent(domainEvent)
                .setEventOccureedOn(storedEvent.occurredOn())
                .setEventId(storedEvent.eventId())
                .setEventClassName(storedEvent.className());

            notifications.add(notification);
        }

        return notifications;
    }
}
