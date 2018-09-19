package xyz.devlxl.chaos.support.domainevents.notification.delivery;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * The mock for DeliveryServiceTest
 * 
 * @author Liu Xiaolei
 * @date 2018/09/12
 */
public interface MockDeliveryConfirmBinding {
    String MOCK1 = "mock1";
    String MOCK2 = "mock2";

    @Input(MockDeliveryConfirmBinding.MOCK1)
    SubscribableChannel mock1();

    @Input(MockDeliveryConfirmBinding.MOCK2)
    SubscribableChannel mock2();
}
