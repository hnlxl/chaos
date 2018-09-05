package xyz.devlxl.chaos.base.domain;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Liu Xiaolei
 * @date 2018/07/21
 */
@Getter
@ToString()
@EqualsAndHashCode()
@Deprecated
public abstract class AbstractDomainEvent implements DomainEvent {
    /** 事件发生时间 */
    protected Date occurredOn;

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
