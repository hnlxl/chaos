package xyz.devlxl.chaos.support.jpa.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Liu Xiaolei
 * @date 2018/09/04
 */
public interface JpaStoredDomainEventRepository extends JpaRepository<JpaStoredDomainEvent, Long> {
    public List<JpaStoredDomainEvent> findByClassNameIsAndOccurredOnBetween(String classNameIs, Date occurredOnFrom,
        Date occurredOnTo);
}
