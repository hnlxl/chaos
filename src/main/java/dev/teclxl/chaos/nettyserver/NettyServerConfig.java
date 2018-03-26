package dev.teclxl.chaos.nettyserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.teclxl.chaos.nettyserver.handler.ChannelConnectEventHandler;
import dev.teclxl.chaos.nettyserver.handler.InMessageDecoder;
import dev.teclxl.chaos.nettyserver.handler.InMessageHandler;
import dev.teclxl.chaos.nettyserver.handler.OutMessageEncoder;
import dev.teclxl.chaos.nettyserver.handler.ServerHeartbeatHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Configuration
public class NettyServerConfig {
    @Autowired
    private NettyServerProperties properties;
    @Autowired
    private NioEventLoopGroup nettyServerBossGroup;
    @Autowired
    private NioEventLoopGroup nettyServerWorkerGroup;
    @Autowired
    private EventExecutorGroup nettyServerBusinessGroup;
    @Autowired
    private ChannelConnectEventHandler channelConnectEventHandler;
    @Autowired
    private ServerHeartbeatHandler serverHeartbeatHandler;
    @Autowired
    private InMessageDecoder inMessageDecoder;
    @Autowired
    private InMessageHandler inMessageHandler;
    @Autowired
    private OutMessageEncoder outMessageEncoder;

    @Bean
    public NioEventLoopGroup nettyServerBossGroup() {
        return new NioEventLoopGroup(properties.getBossThreadCount());
    }

    @Bean
    public NioEventLoopGroup nettyServerWorkerGroup() {
        return new NioEventLoopGroup(properties.getWorkerThreadCount());
    }

    @Bean
    public EventExecutorGroup nettyServerBusinessGroup() {
        return new DefaultEventExecutorGroup(properties.getBusinessThreadCount());
    }

    @Bean
    public ServerBootstrap nettyServerBootstrap() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(nettyServerBossGroup, nettyServerWorkerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(channelInitializer());

        b.option(ChannelOption.SO_BACKLOG, properties.getSoBacklog());
        b.childOption(ChannelOption.SO_KEEPALIVE, properties.isSoKeepalive());

        return b;
    }

    private ChannelInitializer<SocketChannel> channelInitializer() {
        return new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("channelConnectEventHandler", channelConnectEventHandler);
                pipeline.addLast("idleStateHandler", new IdleStateHandler(properties.getMaxReaderIdle(),
                    properties.getMaxWriterIdle(), properties.getMaxAllIdle()));
                pipeline.addLast("serverHeartbeatHandler", serverHeartbeatHandler);
                pipeline.addLast("frameDecoder",
                    new DelimiterBasedFrameDecoder(266, false, true, Unpooled.copiedBuffer("\n".getBytes())));
                pipeline.addLast("inMessageDecoder", inMessageDecoder);
                pipeline.addLast(nettyServerBusinessGroup, "inMessageHandler", inMessageHandler);
                pipeline.addLast("outMessageEncoder", outMessageEncoder);
            }
        };
    }

}
