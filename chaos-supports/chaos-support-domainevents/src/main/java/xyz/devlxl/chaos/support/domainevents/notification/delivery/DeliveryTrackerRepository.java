package xyz.devlxl.chaos.support.domainevents.notification.delivery;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Liu Xiaolei
 * @date 2018/09/11
 */
public interface DeliveryTrackerRepository extends JpaRepository<DeliveryTracker, Byte> {}
