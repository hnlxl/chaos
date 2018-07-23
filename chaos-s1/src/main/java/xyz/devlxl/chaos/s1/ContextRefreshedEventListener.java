package xyz.devlxl.chaos.s1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;
import xyz.devlxl.chaos.s1.channel.NettyServerStarter;

/**
 * 
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@Slf4j
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 临时措施，通过名字判断，对feign造成的刷新，不做任何处理
        String feignContextMark = "Feign";
        if (event.getSource().toString().contains(feignContextMark)) {
            return;
        }

        ApplicationContext appContext = event.getApplicationContext();
        Environment environment = appContext.getBean(Environment.class);

        // 触发Netty Server启动处理
        NettyServerStarter nettyServerStarter = appContext.getBean(NettyServerStarter.class);
        nettyServerStarter.startUp(appContext);
    }

}
