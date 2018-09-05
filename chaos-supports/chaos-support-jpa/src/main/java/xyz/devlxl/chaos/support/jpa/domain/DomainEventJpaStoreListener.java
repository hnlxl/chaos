package xyz.devlxl.chaos.support.jpa.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.Setter;

/**
 * @author Liu Xiaolei
 * @date 2018/09/04
 */
@Component
public class DomainEventJpaStoreListener {
    @Autowired
    private DummyService dummyService;

    @Setter(onMethod_ = @Autowired)
    private JpaStoredDomainEventRepository jpaStoredDomainEventRepository;

    @Setter(onMethod_ = {@Autowired})
    private JpaStoredDomainEventHelper jpaStoredDomainEventHelper;

    @EventListener
    public void store(AbstractJpaDomainEvent domainEvent) throws JsonProcessingException {
        String eventBody = jpaStoredDomainEventHelper.serializeEventBody(domainEvent);

        JpaStoredDomainEvent storedDomainEvent = new JpaStoredDomainEvent()
            .eventBody(eventBody)
            .occurredOn(domainEvent.occurredOn())
            .className(domainEvent.getClass().getName());

        dummyService.beforeSave();
        jpaStoredDomainEventRepository.save(storedDomainEvent);
        dummyService.afterSave();
    }

    @Service
    public static class DummyService {
        public void beforeSave() {}

        public void afterSave() {}
    }
}
