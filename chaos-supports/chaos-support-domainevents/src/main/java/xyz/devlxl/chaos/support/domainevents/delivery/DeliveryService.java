package xyz.devlxl.chaos.support.domainevents.delivery;

import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import xyz.devlxl.chaos.support.domain.StoredDomainEvent;
import xyz.devlxl.chaos.support.domainevents.store.JpaStoredDomainEvent;
import xyz.devlxl.chaos.support.domainevents.store.JpaStoredDomainEventRepository;

/**
 * The application service of the stored domain events's delivery
 * 
 * @author Liu Xiaolei
 * @date 2018/09/11
 */
@Service
@EnableConfigurationProperties(DeliveryProperties.class)
@EnableBinding
@Slf4j
public class DeliveryService {

    @Setter(onMethod_ = @Autowired)
    private DeliveryProperties deliveryProperties;

    @Setter(onMethod_ = @Autowired)
    private JpaStoredDomainEventRepository jpaStoredDomainEventRepository;

    @Setter(onMethod_ = @Autowired)
    private DeliveryTrackerRepository deliveryTrackerRepository;

    @Setter(onMethod_ = @Autowired)
    private BinderAwareChannelResolver binderAwareChannelResolver;

    @Transactional
    public void deliverUndelivered() {
        DeliveryTracker deliveryTracker = deliveryTracker();
        List<JpaStoredDomainEvent> undeliveredAndSortedEvents
            = listUndeliveredSmallToLarge(deliveryTracker.mostRecentDeliveredEventId());

        Long maxEventIdJustDelivered = null;
        // Deliver events in order until all events has been delivered, or any event cannot be delivered.
        for (JpaStoredDomainEvent event : undeliveredAndSortedEvents) {
            if (deliverAnStoredEvent(event)) {
                maxEventIdJustDelivered = event.eventId();
            } else {
                break;
            }
        }

        if (maxEventIdJustDelivered != null) {
            trackMostRecentDeliveredEvent(deliveryTracker, maxEventIdJustDelivered);
        }
    }

    protected DeliveryTracker deliveryTracker() {
        return deliveryTrackerRepository.findById(DeliveryTracker.ID).orElse(
            new DeliveryTracker()
                .mostRecentDeliveredEventId(DeliveryTracker.MIN_DELIVERED_EVENT_ID));

    }

    protected void trackMostRecentDeliveredEvent(DeliveryTracker deliveryTracker, Long mostRecentDeliveredEventId) {
        deliveryTrackerRepository.save(deliveryTracker.mostRecentDeliveredEventId(mostRecentDeliveredEventId));
    }

    protected List<JpaStoredDomainEvent> listUndeliveredSmallToLarge(Long mostRecentDeliveredEventId) {
        return jpaStoredDomainEventRepository
            .findAllByEventIdGreaterThan(mostRecentDeliveredEventId, Sort.by(Order.asc("eventId")));
    }

    protected boolean deliverAnStoredEvent(StoredDomainEvent event) {
        Message<String> message = MessageBuilder.withPayload(event.eventBody())
            .setHeader(deliveryProperties.getHeaderKey().getEventType(), event.className())
            .setHeader(deliveryProperties.getHeaderKey().getEventId(), String.valueOf(event.eventId()))
            .setHeader(deliveryProperties.getHeaderKey().getOccurredOn(), String.valueOf(event.occurredOn().getTime()))
            .build();
        try {
            return wrappingSend(binderAwareChannelResolver.resolveDestination(event.className()), message);
        } catch (Exception e) {
            log.warn("Exception occurs when delivering the domain event!", e);
            return false;
        }
    }

    /**
     * Wrapping method
     * <p>
     * Just to mock a send failure during unit testing. It is difficult to mock MessageChannel.
     * <p>
     * TODO It can't use <code>@SpyBean</code>(but can use <code>@MockBean</code>) for the current class because of
     * unknown reason. Therefore, I temporarily use a special way to implement the mock, but this special way is a
     * violation of the coding principle
     * 
     * @param channel
     * @param message
     * @return
     */
    protected boolean wrappingSend(MessageChannel channel, Message<String> message) {
        boolean isMock = false;
        if (this.isMockSend) {
            if (this.mockSendOnlyIds.isPresent()) {
                isMock = LongStream.of(this.mockSendOnlyIds.get()).anyMatch((oneIdToMock) -> {
                    return oneIdToMock == Long
                        .parseLong(message.getHeaders().get(deliveryProperties.getHeaderKey().getEventId()).toString());
                });
            } else {
                isMock = true;
            }
        }
        if (isMock) {
            switch (this.mockSendType) {
                case 1:
                    return false;
                case 2:
                    throw this.mockSendThrown;
                default:
                    throw new RuntimeException();
            }
        } else {
            return channel.send(message, 500);
        }
    }

    private boolean isMockSend = false;
    private int mockSendType = 0;
    private RuntimeException mockSendThrown = null;
    private Optional<long[]> mockSendOnlyIds = Optional.empty();

    protected void resetSpeicalWay() {
        this.isMockSend = false;
        this.mockSendType = 0;
    }

    protected void mockSendFalse(Optional<long[]> mockSendOnlyIds) {
        this.isMockSend = true;
        this.mockSendType = 1;
        this.mockSendOnlyIds = mockSendOnlyIds;
    }

    protected void mockSendThrown(RuntimeException thrown, Optional<long[]> mockSendOnlyIds) {
        this.isMockSend = true;
        this.mockSendType = 2;
        this.mockSendThrown = thrown;
        this.mockSendOnlyIds = mockSendOnlyIds;
    }
}
