package xyz.devlxl.chaos.s1.application.subscriber.scsdemo;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author Liu Xiaolei
 * @date 2018/07/31
 */
public interface UpperPersonBinding {
    String INPUT = "upper-person-input";

    @Input(UpperPersonBinding.INPUT)
    SubscribableChannel upperPersonInput();
}
