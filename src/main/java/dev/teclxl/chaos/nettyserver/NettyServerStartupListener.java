package dev.teclxl.chaos.nettyserver;

import java.net.InetSocketAddress;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Slf4j
public class NettyServerStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext appContext = event.getApplicationContext();
        NettyServerProperties properties = appContext.getBean(NettyServerProperties.class);
        ServerBootstrap nettyServerBootstrap = (ServerBootstrap)appContext.getBean("nettyServerBootstrap");

        try {
            InetSocketAddress nettyServerSocketAddress = new InetSocketAddress(properties.getPort());
            log.info("Starting up netty server at " + nettyServerSocketAddress);
            ChannelFuture serverChannelFuture = nettyServerBootstrap.bind(nettyServerSocketAddress).sync();
            log.info("Netty server has been started up and listen on " + serverChannelFuture.channel().localAddress());

            // 由于Spring容器回继续维持主线程，所以这里不用“closeFuture().sync()”;

        } catch (InterruptedException e) {
            String msg = "Netty server did not start up successfully, "
                + "can't stop application automatically, "
                + "please stop the application manually";
            log.error(msg, e);
            Thread.currentThread().interrupt();
        }

        // 启动连接通道有效性验证独立线程
    }

}
