package xyz.devlxl.chaos.support.domainevents.notification.delivery;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import lombok.extern.slf4j.Slf4j;

/**
 * The mock for DeliveryServiceTest
 * 
 * @author Liu Xiaolei
 * @date 2018/09/12
 */
@EnableBinding(MockDeliveryConfirmBinding.class)
@Slf4j
public class MockDeliveryConfirmListener {

    @StreamListener(target = MockDeliveryConfirmBinding.MOCK1)
    public void mock1Handle(String messagePayload) {
        log.info("Mock1 handled");
    }

    @StreamListener(target = MockDeliveryConfirmBinding.MOCK2)
    public void mock2Handle(String messagePayload) {
        log.info("Mock2 handled");
    }
}
