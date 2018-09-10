package xyz.devlxl.chaos.support.jpa.domain.eventnotification;

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
import xyz.devlxl.chaos.support.jpa.domain.JpaStoredDomainEvent;
import xyz.devlxl.chaos.support.jpa.domain.JpaStoredDomainEventRepository;
import xyz.devlxl.chaos.support.jpa.domain.JsonSerializationException;

/**
 * The application service of stored domain event's notification
 * 
 * @author Liu Xiaolei
 * @date 2018/09/06
 */
@Service
public class EventNotificationService {
    public static final int LOG_NOTIFICATION_COUNT = 20;

    @Setter(onMethod_ = @Autowired)
    private JpaStoredDomainEventRepository jpaStoredDomainEventRepository;

    @Setter(onMethod_ = {@Qualifier("objectMapperOfJpaSupport"), @Autowired})
    private ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public EventNotificationLog currentNotificationLog() {
        return findNotificationLog(calculateCurrentNotificationLogId());
    }

    @Transactional(readOnly = true)
    public EventNotificationLog eventNotificationLog(String notificationLogId) {
        return findNotificationLog(EventNotificationLogId.from(notificationLogId));
    }

    @Transactional
    public void publishNotifications() {
        // TODO
    }

    protected EventNotificationLogId calculateCurrentNotificationLogId() {
        // Using MAX instead of COUNT，so we don't have to ask for the ID to start from one and be continuous.
        long max = jpaStoredDomainEventRepository.maxId();

        long remainder = max % LOG_NOTIFICATION_COUNT;
        if (remainder == 0) {
            remainder = LOG_NOTIFICATION_COUNT;
        }

        long low = max - remainder + 1;
        // 虽然当前有可能不存在一整套的通知，但是日志的id应该是暂新的
        long high = low + LOG_NOTIFICATION_COUNT - 1;

        return new EventNotificationLogId(low, high);
    }

    protected EventNotificationLog findNotificationLog(EventNotificationLogId eventNotificationLogId) {
        List<JpaStoredDomainEvent> storedEvents
            = jpaStoredDomainEventRepository.findAllByEventIdBetween(eventNotificationLogId.low(),
                eventNotificationLogId.high(), Sort.by(Order.asc("eventId")));
        List<EventNotification> notifications = notificationsFrom(storedEvents);

        long max = jpaStoredDomainEventRepository.maxId();
        boolean archived = eventNotificationLogId.high() < max;

        Optional<String> next = Optional.empty();
        if (archived) {
            next = Optional.of(eventNotificationLogId.next().encoded());
        }

        Optional<String> previous = eventNotificationLogId.previous()
            .map((previousId) -> {
                return previousId.encoded();
            });

        EventNotificationLog log = new EventNotificationLog()
            .setSelf(eventNotificationLogId.encoded())
            .setNext(next)
            .setPrevious(previous)
            .setNotifications(notifications)
            .setArchived(archived);

        return log;
    }

    protected List<EventNotification> notificationsFrom(List<JpaStoredDomainEvent> storedEvents) {
        List<EventNotification> notifications = new ArrayList<>(storedEvents.size());
        for (JpaStoredDomainEvent storedEvent : storedEvents) {
            DomainEvent domainEvent = null;
            try {
                domainEvent = (DomainEvent)objectMapper.readValue(
                    storedEvent.eventBody(),
                    Class.forName(storedEvent.className()));
            } catch (IOException | ClassNotFoundException e) {
                throw new JsonSerializationException(e);
            }

            EventNotification notification = new EventNotification()
                .setEvent(domainEvent)
                .setEventOccureedOn(storedEvent.occurredOn())
                .setEventId(storedEvent.eventId())
                .setEventClassName(storedEvent.className());

            notifications.add(notification);
        }

        return notifications;
    }
}
