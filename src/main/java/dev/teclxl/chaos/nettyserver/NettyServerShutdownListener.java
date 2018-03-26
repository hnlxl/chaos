package dev.teclxl.chaos.nettyserver;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import dev.teclxl.chaos.application.context.GeneralContext;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Slf4j
public class NettyServerShutdownListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        ApplicationContext appContext = event.getApplicationContext();
        NioEventLoopGroup nettyServerBossGroup = (NioEventLoopGroup)appContext.getBean("nettyServerBossGroup");
        NioEventLoopGroup nettyServerWorkerGroup = (NioEventLoopGroup)appContext.getBean("nettyServerWorkerGroup");
        GeneralContext generalContext = (GeneralContext)appContext.getBean(GeneralContext.class);

        // 通知连接通道有效性验证独立线程进行关闭处理

        log.info("Shuting down netty server");
        try {
            nettyServerWorkerGroup.shutdownGracefully().sync();
            nettyServerBossGroup.shutdownGracefully().sync();
            log.info("Shuted down  netty server");
        } catch (InterruptedException e) {
            log.warn("Netty server did not shut down successfully", e);
            Thread.currentThread().interrupt();
        }

    }

}
