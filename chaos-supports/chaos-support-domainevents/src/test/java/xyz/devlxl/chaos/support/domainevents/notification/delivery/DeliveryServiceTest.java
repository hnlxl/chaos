package xyz.devlxl.chaos.support.domainevents.notification.delivery;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hamcrest.CustomTypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.cloud.stream.test.matcher.MessageQueueMatcher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import xyz.devlxl.chaos.support.domain.AbstractDomainEvent;
import xyz.devlxl.chaos.support.domain.DomainEvent;
import xyz.devlxl.chaos.support.domainevents.notification.NotificationReader;
import xyz.devlxl.chaos.support.domainevents.store.JpaStoredDomainEvent;
import xyz.devlxl.chaos.support.domainevents.store.JpaStoredDomainEventRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeliveryServiceTest {
    @Autowired
    private DeliveryService service;
    @Autowired
    private JpaStoredDomainEventRepository eventStoreRepo;
    @Autowired
    private DeliveryTrackerRepository deliveryTrackerRepository;
    @Autowired
    private MessageCollector messageCollector;
    @Autowired
    private BinderAwareChannelResolver binderAwareChannelResolver;
    @Autowired
    private DeliveryProperties deliveryProperties;
    @Qualifier("objectMapperOfDomainEventsSupport")
    @Autowired
    private ObjectMapper objectMapper;

    @Rule
    public OutputCapture capture = new OutputCapture();

    @Test
    @Transactional
    @Rollback
    public final void testDeliverUndeliveredStroedEvent() throws JsonProcessingException {
        // prepare
        eventStoreRepo.deleteAll();
        deliveryTrackerRepository.deleteAll();
        BlockingQueue<Message<?>> messageCollected = null;

        // when no tracker
        assertFalse(deliveryTrackerRepository.findById(DeliveryTracker.ID).isPresent());
        service.deliverUndeliveredStroedEvent();
        assertFalse(deliveryTrackerRepository.findById(DeliveryTracker.ID).isPresent());

        // append 5 events and then execute twice. In addition, determine the reference value of auto-incrementing
        appendNewEvents(5, new DummyFirstEvent());
        long autoInCreReference = eventStoreRepo.maxId() - 5;
        messageCollected = messageCollector
            .forChannel(binderAwareChannelResolver.resolveDestination(DummyFirstEvent.class.getName()));
        service.deliverUndeliveredStroedEvent();
        assertEquals(deliveryTrackerRepository.findById(DeliveryTracker.ID).get().mostRecentDeliveredEventId(),
            Long.valueOf(autoInCreReference + 5));
        for (int i = 1; i <= 5; i++) {
            assertThat(messageCollected,
                messageQueueMatcher(Long.valueOf(autoInCreReference + i), DummyFirstEvent.class));
        }

        service.deliverUndeliveredStroedEvent();
        assertEquals(deliveryTrackerRepository.findById(DeliveryTracker.ID).get().mostRecentDeliveredEventId(),
            Long.valueOf(autoInCreReference + 5));
        assertTrue(messageCollected.size() == 0);

        // append 5 events, set returning false when sending the events that id>7
        appendNewEvents(5, new DummySecondEvent());
        messageCollected = messageCollector
            .forChannel(binderAwareChannelResolver.resolveDestination(DummySecondEvent.class.getName()));
        service.mockSendFalse(Optional.of(new long[] {autoInCreReference + 8,
            autoInCreReference + 9,
            autoInCreReference + 10}));
        service.deliverUndeliveredStroedEvent();
        assertEquals(deliveryTrackerRepository.findById(DeliveryTracker.ID).get().mostRecentDeliveredEventId(),
            Long.valueOf(autoInCreReference + 7));
        assertThat(
            messageCollected.stream()
                .map(message -> message.getHeaders().get(deliveryProperties.getHeaderKey().getEventType()))
                .collect(Collectors.toList()),
            hasItem(equalTo(DummySecondEvent.class.getName())));
        assertTrue(messageCollected.size() == 2);
        messageCollected.clear();
        service.resetSpeicalWay();

        // set throwing when sending
        service.mockSendThrown(new NullPointerException(), Optional.empty());
        service.deliverUndeliveredStroedEvent();
        assertEquals(deliveryTrackerRepository.findById(DeliveryTracker.ID).get().mostRecentDeliveredEventId(),
            Long.valueOf(autoInCreReference + 7));
        assertTrue(messageCollected.size() == 0);
        service.resetSpeicalWay();
    }

    @Test
    public final void testDeliveryCommondEvent() {
        String type = "command.dummy.FirstCommand";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("paramOne", "hello");
        parameters.put("paramTwo", "world");
        parameters.put("paramThree", 1234);
        Date occurredOn = new Date();
        occurredOn.setTime(123456789);
        Date startTestOn = new Date();
        Long notificationId = 987654L;
        MessageChannel messageChannel = binderAwareChannelResolver.resolveDestination(type);
        BlockingQueue<Message<?>> messageCollected = messageCollector.forChannel(messageChannel);

        // test 1, all parameters
        service.deliveryCommondEvent(type, parameters, occurredOn, notificationId);
        assertThat(messageCollected, MessageQueueMatcher.receivesMessageThat(
            new CustomTypeSafeMatcher<Message<String>>("") {
                @Override
                protected boolean matchesSafely(Message<String> message) {
                    MessageHeaders headers = message.getHeaders();
                    boolean headerCheck
                        = headers.get(deliveryProperties.getHeaderKey().getNotificationId())
                            .equals(String.valueOf(notificationId))
                            && headers.get(deliveryProperties.getHeaderKey().getEventType())
                                .equals(type)
                            && headers.get(deliveryProperties.getHeaderKey().getOccurredOn())
                                .equals(String.valueOf(occurredOn.getTime()));

                    NotificationReader reader = new NotificationReader(objectMapper, message.getPayload());
                    boolean payloadCheck
                        = reader.longValue("notificationId").filter(notificationId::equals).isPresent()
                            && reader.stringValue("typeName").filter(type::equals).isPresent()
                            && reader.dateValue("occurredOn").filter(occurredOn::equals).isPresent()
                            && reader.eventDateValue("occurredOn").filter(occurredOn::equals).isPresent()
                            && reader.eventStringValue("paramOne").filter("hello"::equals).isPresent()
                            && reader.eventStringValue("paramTwo").filter("world"::equals).isPresent()
                            && reader.eventIntegerValue("paramThree").filter(Integer.valueOf(1234)::equals).isPresent();

                    return headerCheck && payloadCheck;
                }
            }));

        // test 2, default occurredOn
        service.deliveryCommondEvent(type, parameters, null, notificationId);
        assertThat(messageCollected, MessageQueueMatcher.receivesMessageThat(
            new CustomTypeSafeMatcher<Message<String>>("") {
                @Override
                protected boolean matchesSafely(Message<String> message) {
                    long headerValue = Long
                        .parseLong((String)message.getHeaders().get(deliveryProperties.getHeaderKey().getOccurredOn()));
                    NotificationReader reader = new NotificationReader(objectMapper, message.getPayload());
                    long notificationValue = reader.dateValue("occurredOn").get().getTime();
                    long eventValue = reader.eventDateValue("occurredOn").get().getTime();
                    return headerValue == notificationValue
                        && notificationValue == eventValue
                        && headerValue - startTestOn.getTime() > 0
                        && headerValue - startTestOn.getTime() < 5000;
                }
            }));

        // test 3, default notificationId
        service.deliveryCommondEvent(type, parameters, occurredOn, null);
        assertThat(messageCollected, MessageQueueMatcher.receivesMessageThat(
            new CustomTypeSafeMatcher<Message<String>>("") {
                @Override
                protected boolean matchesSafely(Message<String> message) {
                    long headerValue = Long
                        .parseLong(
                            (String)message.getHeaders().get(deliveryProperties.getHeaderKey().getNotificationId()));
                    NotificationReader reader = new NotificationReader(objectMapper, message.getPayload());
                    long notificationValue = reader.longValue("notificationId").get();
                    long eventValue = reader.longValue("notificationId").get();
                    return headerValue == notificationValue
                        && notificationValue == eventValue
                        && headerValue == -1;
                }
            }));
    }

    private List<JpaStoredDomainEvent> appendNewEvents(int number, DomainEvent eventTemplateObj)
        throws JsonProcessingException {
        List<JpaStoredDomainEvent> events = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            events.add(new JpaStoredDomainEvent()
                .eventBody(objectMapper.writeValueAsString(eventTemplateObj))
                .occurredOn(new Date())
                .className(eventTemplateObj.getClass().getName()));
        }

        return eventStoreRepo.saveAll(events);
    }

    @Setter
    @Getter
    public static class DummyFirstEvent extends AbstractDomainEvent {
        public String simpleName = DummyFirstEvent.class.getSimpleName();
    }

    @Setter
    @Getter
    public static class DummySecondEvent extends AbstractDomainEvent {
        public String simpleName = DummySecondEvent.class.getSimpleName();
    }

    private MessageQueueMatcher<String> messageQueueMatcher(Long expectedEventId,
        Class<?> expectedEventClz) {
        String expectedEventIdInString = String.valueOf(expectedEventId);
        String failMessage = String.format("about a eligible notification with eventId is %d and eventType is %s",
            expectedEventId, expectedEventClz.getName());
        return MessageQueueMatcher.receivesMessageThat(
            new CustomTypeSafeMatcher<Message<String>>(failMessage) {
                @Override
                protected boolean matchesSafely(Message<String> message) {
                    MessageHeaders headers = message.getHeaders();
                    boolean headerCheck
                        = headers.get(deliveryProperties.getHeaderKey().getNotificationId())
                            .equals(expectedEventIdInString)
                            && headers.get(deliveryProperties.getHeaderKey().getEventType())
                                .equals(expectedEventClz.getName());

                    NotificationReader reader = new NotificationReader(objectMapper, message.getPayload());
                    boolean payloadCheck
                        = reader.longValue("notificationId").filter(expectedEventId::equals).isPresent()
                            && reader.stringValue("typeName").filter(expectedEventClz.getName()::equals).isPresent()
                            && reader.eventStringValue("simpleName")
                                .filter(expectedEventClz.getSimpleName()::equals).isPresent();

                    return headerCheck && payloadCheck;
                }
            });
    }
}
