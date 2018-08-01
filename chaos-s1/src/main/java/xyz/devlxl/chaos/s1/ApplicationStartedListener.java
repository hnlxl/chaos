package xyz.devlxl.chaos.s1;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;
import xyz.devlxl.chaos.s1.channel.NettyServerStarter;

/**
 * 应用启动后监听器，在应用启动后做一些处理。
 * 
 * <p>
 * 通过以下方式实现：监听ContextRefreshedEvent，但只在第一次该事件触发时采取行动，忽略之后的事件。 这意味该监听器的启动时机，是最高父容器/默认容器初始化完成时。
 * 
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@Slf4j
public class ApplicationStartedListener implements ApplicationListener<ContextRefreshedEvent> {
    private final AtomicInteger listenerNum = new AtomicInteger(0);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 仅第一次有实际处理过程，后面的，仅计数
        if (listenerNum.getAndIncrement() == 0) {
            process(event);
        }

        log.info("ApplicationStartedListener has been triggered [" + listenerNum.get() + "] times");
    }

    private void process(ContextRefreshedEvent event) {
        ApplicationContext appContext = event.getApplicationContext();
        Environment environment = appContext.getBean(Environment.class);

        // 触发Netty Server启动处理
        NettyServerStarter nettyServerStarter = appContext.getBean(NettyServerStarter.class);
        nettyServerStarter.startUp(appContext);

        // ignore "no use" waring
        log.debug(environment.toString());
    }

}
