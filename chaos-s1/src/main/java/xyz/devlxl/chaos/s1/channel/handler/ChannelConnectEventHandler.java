package xyz.devlxl.chaos.s1.channel.handler;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 通道连接事件的处理器。
 * <p>
 * 通道连接激活、连接断开的所有事件都统一在这里处理。以后可能会包含心跳连接相关事件（应该是只含服务器主动向终端发送心跳连接的事件监听和处理，不包含终端向服务器发送心跳消息的处理）。
 * 关于断开连接，这里只包含断开连接后的事件响应处理，不包含断开连接前的业务处理，以及断开连接这个事件源。
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Slf4j
@Component
@Sharable
public class ChannelConnectEventHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Channel active : " + ctx.channel().toString());

        // 将连接放入等待有效性判断的延迟队列

        // 不再继续传播事件
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Channel inactive : " + ctx.channel().toString());
        // 不再继续传播事件
    }
}
