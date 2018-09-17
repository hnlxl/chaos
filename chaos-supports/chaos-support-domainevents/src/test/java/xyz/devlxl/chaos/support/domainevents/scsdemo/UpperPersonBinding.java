package xyz.devlxl.chaos.support.domainevents.scsdemo;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author Liu Xiaolei
 * @date 2018/07/31
 */
public interface UpperPersonBinding {
    // Need to configure the "destination" of "UpperPersonBinding.INPUT" and "PersonBinding.OUTPUT" to be the same
    String INPUT = "upper-person-input";

    @Input(UpperPersonBinding.INPUT)
    SubscribableChannel upperPersonInput();
}
