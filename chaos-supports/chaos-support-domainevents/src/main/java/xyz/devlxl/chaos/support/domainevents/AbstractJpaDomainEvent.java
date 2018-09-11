package xyz.devlxl.chaos.support.domainevents;

import xyz.devlxl.chaos.support.domain.AbstractDomainEvent;

/**
 * The abstract base class of the domain event of the JPA-related domain model. This class is only used as a type tag
 * and has no features. Only this type can be listened to by DomainEventJpaStoreListener.
 * 
 * @author Liu Xiaolei
 * @date 2018/09/05
 */
public class AbstractJpaDomainEvent extends AbstractDomainEvent {

}
