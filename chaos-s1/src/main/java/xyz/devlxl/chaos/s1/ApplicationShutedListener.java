package xyz.devlxl.chaos.s1;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import lombok.extern.slf4j.Slf4j;
import xyz.devlxl.chaos.s1.channel.NettyServerShuter;

/**
 * 应用关闭后监听器，在应用关闭时做一些处理。
 * 
 * <p>
 * 通过以下方式实现：监听ContextClosedEvent，但只在第一次该事件触发时采取行动，忽略之后的事件。 这意味该监听器的启动时机，是最高父容器/默认容器关闭时。
 * 
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@Slf4j
public class ApplicationShutedListener implements ApplicationListener<ContextClosedEvent> {
    private final AtomicInteger listenerNum = new AtomicInteger(0);

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // 仅第一次有实际处理过程，后面的，仅计数
        if (listenerNum.getAndIncrement() == 0) {
            process(event);
        }

        log.info("ApplicationShutedListener has been triggered [" + listenerNum.get() + "] times");
    }

    public void process(ContextClosedEvent event) {
        ApplicationContext appContext = event.getApplicationContext();

        // 触发Netty Server关闭处理
        NettyServerShuter nettyServerShuter = appContext.getBean(NettyServerShuter.class);
        nettyServerShuter.shutDown(appContext);
    }
}
