package xyz.devlxl.chaos.s1.channel;

import java.net.InetSocketAddress;

import org.springframework.context.ApplicationContext;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import xyz.devlxl.chaos.base.properties.S1NettyServerProperties;

/**
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Slf4j
public class NettyServerStarter {

    public void startUp(ApplicationContext appContext) {
        S1NettyServerProperties properties = appContext.getBean(S1NettyServerProperties.class);
        if (!properties.isActuallyStarted()) {
            return;
        }

        ServerBootstrap nettyServerBootstrap = (ServerBootstrap)appContext.getBean("nettyServerBootstrap");

        try {
            InetSocketAddress nettyServerSocketAddress = new InetSocketAddress(properties.getPort());
            log.info("Starting up netty server at " + nettyServerSocketAddress);
            ChannelFuture serverChannelFuture = nettyServerBootstrap.bind(nettyServerSocketAddress).sync();
            log.info("Netty server has been started up and listen on " + serverChannelFuture.channel().localAddress());

            // 由于Spring容器回继续维持主线程，所以这里不用“closeFuture().sync()”;

        } catch (InterruptedException e) {
            String msg = "Netty server did not start up successfully.";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }

        // 启动相关HashedWheelTimer
    }

}
