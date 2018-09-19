package xyz.devlxl.chaos.support.domainevents.notification.delivery;

import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.router.ExpressionEvaluatingRouter;

/**
 * @author Liu Xiaolei
 * @date 2018/09/14
 */
// @Configuration
// TODO 加上后，不管是support测试，还是连接正式服务器，都不能正常投递消息。表现为1个成功1个，2个成功1个，5个成功2个，6个成功3个。原因未知
public class DeliveryConfiguration {
    @Bean(name = "deliverChannelRouter")
    @ServiceActivator(inputChannel = DeliveryBinding.DELIVERY)
    public ExpressionEvaluatingRouter deliverChannelRouter(BinderAwareChannelResolver resolver) {
        ExpressionEvaluatingRouter router
            = new ExpressionEvaluatingRouter(new SpelExpressionParser().parseExpression("headers.topic"));
        router.setDefaultOutputChannel(null);
        router.setChannelResolver(resolver);
        return router;
    }
}
