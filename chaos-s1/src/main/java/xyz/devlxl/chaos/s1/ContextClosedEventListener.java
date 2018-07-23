package xyz.devlxl.chaos.s1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import xyz.devlxl.chaos.s1.channel.NettyServerShuter;

/**
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // 临时措施，通过名字判断，对feign造成的刷新，不做任何处理
        String feignContextMark = "Feign";
        if (event.getSource().toString().contains(feignContextMark)) {
            return;
        }

        ApplicationContext appContext = event.getApplicationContext();

        // 触发Netty Server关闭处理
        NettyServerShuter nettyServerShuter = appContext.getBean(NettyServerShuter.class);
        nettyServerShuter.shutDown(appContext);
    }
}
