package xyz.devlxl.chaos.support.domainevents.store;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.devlxl.chaos.support.domain.StoredDomainEvent;

/**
 * @author Liu Xiaolei
 * @date 2018/09/03
 */
@Data
@Accessors(fluent = true)
@Entity
@Table(name = "event_store")
public class JpaStoredDomainEvent implements StoredDomainEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long eventId;

    protected String eventBody;
    protected Date occurredOn;
    protected String className;
}
