package xyz.devlxl.chaos.s1.channel;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Slf4j
@Component
public class NettyServerShuter {

    public void shutDown(ApplicationContext appContext) {
        NioEventLoopGroup nettyServerBossGroup = (NioEventLoopGroup)appContext.getBean("nettyServerBossGroup");
        NioEventLoopGroup nettyServerWorkerGroup = (NioEventLoopGroup)appContext.getBean("nettyServerWorkerGroup");

        // 关闭相关HashedWheelTimer

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
