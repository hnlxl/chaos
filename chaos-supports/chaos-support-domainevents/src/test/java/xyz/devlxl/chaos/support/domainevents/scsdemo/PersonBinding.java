package xyz.devlxl.chaos.support.domainevents.scsdemo;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author Liu Xiaolei
 * @date 2018/07/31
 */
public interface PersonBinding {
    String INPUT = "person-input";
    String OUTPUT = "person-output";

    @Input(PersonBinding.INPUT)
    SubscribableChannel personInput();

    @Output(PersonBinding.OUTPUT)
    MessageChannel personOutput();
}
