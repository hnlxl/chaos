package xyz.devlxl.chaos.support.domainevents.store;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Liu Xiaolei
 * @date 2018/09/04
 */
public interface JpaStoredDomainEventRepository extends JpaRepository<JpaStoredDomainEvent, Long> {
    public List<JpaStoredDomainEvent> findAllByClassNameIsAndOccurredOnBetween(String classNameIs, Date occurredOnFrom,
        Date occurredOnTo);

    public List<JpaStoredDomainEvent> findAllByEventIdBetween(Long low, Long high);

    public List<JpaStoredDomainEvent> findAllByEventIdBetween(Long low, Long high, Sort sort);

    @Query("SELECT max(eventId) FROM JpaStoredDomainEvent")
    public Long maxId();
}
