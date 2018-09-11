package xyz.devlxl.chaos.support.domainevents.store;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Setter;
import xyz.devlxl.chaos.support.domain.DomainEvent;

/**
 * @author Liu Xiaolei
 * @date 2018/09/05
 */
@Component
public class JpaStoredDomainEventHelper {
    @Setter(onMethod_ = @Autowired)
    private JpaStoredDomainEventRepository jpaStoredDomainEventRepository;

    @Setter(onMethod_ = {@Qualifier("objectMapperOfJpaSupport"), @Autowired})
    private ObjectMapper objectMapper;

    protected String serializeEventBody(DomainEvent domainEvent) {
        try {
            return objectMapper.writeValueAsString(domainEvent);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException(e);
        }
    }

    /**
     * Just to aid unit testing. Confirm if the event has been stored.
     * 
     * @param event
     * @param occurredOnAdjustment The adjustment range of the event occurrence time. It's unit is millisecond, and
     *        using a value less than or equal to zero to indicate no adjustment. Set this parameter to reduce or ignore
     *        the interference of the event occurrence time when judging
     * @return whether the event has been stored
     */
    public boolean eventStored(DomainEvent event, long occurredOnAdjustment) {
        Assert.notNull(event, "The reference object cannot be null");
        if (occurredOnAdjustment < 0) {
            occurredOnAdjustment = 0;
        }

        List<JpaStoredDomainEvent> fisterFilterEvents = jpaStoredDomainEventRepository
            .findAllByClassNameIsAndOccurredOnBetween(
                event.getClass().getName(),
                event.occurredOn(),
                new Date(event.occurredOn().getTime() + occurredOnAdjustment));
        for (JpaStoredDomainEvent storedEvent : fisterFilterEvents) {
            DomainEvent deserializedEvent = null;
            try {
                deserializedEvent = objectMapper.readValue(storedEvent.eventBody(), event.getClass());
            } catch (IOException e) {
                throw new JsonSerializationException(e);
            }
            if (deserializedEvent.equalsExcludedOccurTime(event)) {
                return true;
            }
        }
        return false;
    }
}
