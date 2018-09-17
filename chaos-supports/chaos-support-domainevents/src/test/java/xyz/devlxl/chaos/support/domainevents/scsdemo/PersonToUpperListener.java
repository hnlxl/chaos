package xyz.devlxl.chaos.support.domainevents.scsdemo;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Liu Xiaolei
 * @date 2018/07/31
 */
@EnableBinding(PersonBinding.class)
@Slf4j
public class PersonToUpperListener {

    @StreamListener(PersonBinding.INPUT)
    @SendTo(PersonBinding.OUTPUT)
    public Person toUpper(Person person) throws InterruptedException {
        log.info("======Original Person=====: " + person);
        person.setName(person.getName().toUpperCase());

        return person;
    }
}
