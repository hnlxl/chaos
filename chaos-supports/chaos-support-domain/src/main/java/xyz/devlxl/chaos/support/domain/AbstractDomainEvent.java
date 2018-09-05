package xyz.devlxl.chaos.support.domain;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The abstract base class for domain events with common functionality. The abstract function is:
 * <li>Implement the DomainEvent interface.
 * <li>Provide the occurredOn field and its constructor. The occurredOn defaults to current time.
 * <p>
 * Subclasses can do the following extensions according to their needs: generate constructors from superclass
 * 
 * @author Liu Xiaolei
 * @date 2018/08/28
 */
@Getter
@ToString()
@EqualsAndHashCode()
public abstract class AbstractDomainEvent implements DomainEvent {
    protected Date occurredOn;

    /**
     * The default constructor. This constructor will set the default value for the generic field.
     * <p>
     * <strong>Note: Constructor calls super() by default, so if the subclass has no special constructors, this
     * constructor will always be called.</strong>
     */
    public AbstractDomainEvent() {
        this(new Date());
    }

    public AbstractDomainEvent(Date occurredOn) {
        this.occurredOn = occurredOn != null ? occurredOn : new Date();
    }

    @Override
    public Date occurredOn() {
        return occurredOn;
    }
}
