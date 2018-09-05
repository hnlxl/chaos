package xyz.devlxl.chaos.base.domain;

import java.util.Date;

/**
 * @author Liu Xiaolei
 * @date 2018/07/21
 */
@Deprecated
public interface DomainEvent {
    /**
     * occurredOn
     * 
     * @return
     */
    public Date occurredOn();
}
