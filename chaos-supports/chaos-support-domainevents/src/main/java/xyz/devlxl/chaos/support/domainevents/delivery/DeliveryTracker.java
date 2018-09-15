package xyz.devlxl.chaos.support.domainevents.delivery;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author Liu Xiaolei
 * @date 2018/09/11
 */
@Data
@Accessors(fluent = true)
@Entity
public class DeliveryTracker {
    public static final Byte ID = 1;
    public static final Long MIN_DELIVERED_EVENT_ID = 0L;

    @Id
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Byte trackerId = ID;

    private Long mostRecentDeliveredEventId;
}
