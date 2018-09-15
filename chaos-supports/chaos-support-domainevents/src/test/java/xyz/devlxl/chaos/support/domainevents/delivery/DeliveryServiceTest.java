package xyz.devlxl.chaos.support.domainevents.delivery;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Rule
    public OutputCapture capture = new OutputCapture();

    @Test
    @Transactional
    @Rollback
    public final void testDeliverUndelivered() {
        // prepare
        eventStoreRepo.deleteAll();
        deliveryTrackerRepository.deleteAll();
        BlockingQueue<Message<?>> messageCollected = null;

        // when no tracker
        assertFalse(deliveryTrackerRepository.findById(DeliveryTracker.ID).isPresent());
        service.deliverUndelivered();
        assertFalse(deliveryTrackerRepository.findById(DeliveryTracker.ID).isPresent());

        // append 5 events and then execute twice. In addition, determine the reference value of auto-incrementing
        appendNewEvents(5, "demo.FirstEvent");
        long autoInCreReference = eventStoreRepo.maxId() - 5;
        messageCollected
            = messageCollector.forChannel(binderAwareChannelResolver.resolveDestination("demo.FirstEvent"));
        service.deliverUndelivered();
        assertEquals(deliveryTrackerRepository.findById(DeliveryTracker.ID).get().mostRecentDeliveredEventId(),
            Long.valueOf(autoInCreReference + 5));
        assertThat(
            messageCollected.stream()
                .map(message -> message.getHeaders().get(deliveryProperties.getHeaderKey().getEventType()))
                .collect(Collectors.toList()),
            hasItem(equalTo("demo.FirstEvent")));
        assertTrue(messageCollected.size() == 5);
        messageCollected.clear();

        service.deliverUndelivered();
        assertEquals(deliveryTrackerRepository.findById(DeliveryTracker.ID).get().mostRecentDeliveredEventId(),
            Long.valueOf(autoInCreReference + 5));
        assertTrue(messageCollected.size() == 0);

        // append 5 events, set returning false when sending the events that id>7
        appendNewEvents(5, "demo.SecondEvent");
        messageCollected
            = messageCollector.forChannel(binderAwareChannelResolver.resolveDestination("demo.SecondEvent"));
        service.mockSendFalse(Optional.of(new long[] {autoInCreReference + 8,
            autoInCreReference + 9,
            autoInCreReference + 10}));
        service.deliverUndelivered();
        assertEquals(deliveryTrackerRepository.findById(DeliveryTracker.ID).get().mostRecentDeliveredEventId(),
            Long.valueOf(autoInCreReference + 7));
        assertThat(
            messageCollected.stream()
                .map(message -> message.getHeaders().get(deliveryProperties.getHeaderKey().getEventType()))
                .collect(Collectors.toList()),
            hasItem(equalTo("demo.SecondEvent")));
        assertTrue(messageCollected.size() == 2);
        messageCollected.clear();
        service.resetSpeicalWay();

        // set throwing when sending
        service.mockSendThrown(new NullPointerException(), Optional.empty());
        service.deliverUndelivered();
        assertEquals(deliveryTrackerRepository.findById(DeliveryTracker.ID).get().mostRecentDeliveredEventId(),
            Long.valueOf(autoInCreReference + 7));
        assertTrue(messageCollected.size() == 0);
        service.resetSpeicalWay();
    }

    private List<JpaStoredDomainEvent> appendNewEvents(int number, String className) {
        List<JpaStoredDomainEvent> events = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            events.add(new JpaStoredDomainEvent()
                .eventBody("eventBody: " + className)
                .occurredOn(new Date())
                .className(className));
        }

        return eventStoreRepo.saveAll(events);
    }
}
