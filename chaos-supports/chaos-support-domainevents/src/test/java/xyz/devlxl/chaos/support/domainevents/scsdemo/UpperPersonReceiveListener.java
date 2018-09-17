package xyz.devlxl.chaos.support.domainevents.scsdemo;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Liu Xiaolei
 * @date 2018/07/31
 */
@EnableBinding(UpperPersonBinding.class)
@Slf4j
public class UpperPersonReceiveListener {

    @StreamListener(UpperPersonBinding.INPUT)
    public void handle(Person person) {
        log.info("======Uppered Person=====: " + person);
    }
}
