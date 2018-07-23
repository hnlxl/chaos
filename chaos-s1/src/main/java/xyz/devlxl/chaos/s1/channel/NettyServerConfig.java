package xyz.devlxl.chaos.s1.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
import xyz.devlxl.chaos.base.properties.S1NettyServerProperties;
import xyz.devlxl.chaos.s1.channel.handler.ChannelConnectEventHandler;
import xyz.devlxl.chaos.s1.channel.handler.IdleEventHandler;
import xyz.devlxl.chaos.s1.channel.handler.InboundMessageDecoder;
import xyz.devlxl.chaos.s1.channel.handler.InboundMessageHandler;
import xyz.devlxl.chaos.s1.channel.handler.OutboundMessageEncoder;

/**
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Configuration
public class NettyServerConfig {
    @Autowired
    private S1NettyServerProperties properties;
    @Autowired
    private NioEventLoopGroup nettyServerBossGroup;
    @Autowired
    private NioEventLoopGroup nettyServerWorkerGroup;
    @Autowired
    private EventExecutorGroup nettyServerBusinessGroup;
    @Autowired
    private ChannelConnectEventHandler channelConnectEventHandler;
    @Autowired
    private IdleEventHandler idleEventHandler;
    @Autowired
    private InboundMessageDecoder inboundMessageDecoder;
    @Autowired
    private InboundMessageHandler inboundMessageHandler;
    @Autowired
    private OutboundMessageEncoder outboundMessageEncoder;

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
                pipeline.addLast("idleEventHandler", idleEventHandler);
                pipeline.addLast("frameDecoder",
                    new DelimiterBasedFrameDecoder(266, false, true, Unpooled.copiedBuffer("\n".getBytes())));
                pipeline.addLast("inboundMessageDecoder", inboundMessageDecoder);
                pipeline.addLast(nettyServerBusinessGroup, "inboundMessageHandler", inboundMessageHandler);
                pipeline.addLast("outboundMessageEncoder", outboundMessageEncoder);
            }
        };
    }

}
