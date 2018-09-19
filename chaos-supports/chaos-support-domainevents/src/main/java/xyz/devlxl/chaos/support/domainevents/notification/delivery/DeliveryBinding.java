package xyz.devlxl.chaos.support.domainevents.notification.delivery;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author Liu Xiaolei
 * @date 2018/09/11
 */
// TODO，因为ExpressionEvaluatingRouter不能使用的原因，此绑定也不再使用，直接使用BinderAwareChannelResolver
public interface DeliveryBinding {
    String DELIVERY = "event-delivery";

    @Output(DeliveryBinding.DELIVERY)
    MessageChannel delivery();
}
