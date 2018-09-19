package xyz.devlxl.chaos.support.domainevents.notification.rest;

import java.util.Optional;

import org.springframework.util.Assert;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Liu Xiaolei
 * @date 2018/09/06
 */
@Getter
@EqualsAndHashCode
@ToString
@Accessors(fluent = true)
public class NotificationLogId {
    private final Long low;
    private final Long high;
    private final int count;

    public static NotificationLogId from(String notificationLogIdText) {
        String[] textIds = notificationLogIdText.split(",");
        return new NotificationLogId(Long.parseLong(textIds[0]), Long.parseLong(textIds[1]));
    }

    public NotificationLogId(Long low, Long high) {
        super();
        Assert.isTrue(low < high && low >= 0, "Thw LOW must be less than the HIGH and greater than zero!");
        this.low = low;
        this.high = high;
        this.count = (int)((this.high - this.low) + 1);
        Assert.isTrue((low - 1) % count == 0, "The LOW or HIGH must be able to divisible by their difference");
    }

    public String encoded() {
        return "" + low + "," + high;
    }

    public NotificationLogId next() {
        return new NotificationLogId(low + count, high + count);
    }

    public Optional<NotificationLogId> previous() {
        if (low < count) {
            return Optional.empty();
        } else {
            return Optional.of(new NotificationLogId(low - count, high - count));
        }
    }
}
