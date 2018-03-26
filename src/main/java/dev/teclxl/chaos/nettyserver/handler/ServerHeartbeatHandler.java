package dev.teclxl.chaos.nettyserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.teclxl.chaos.nettyserver.ChannelCloser;
import dev.teclxl.chaos.nettyserver.OutMessage;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务器端心跳消息处理器
 * 
 * <p>
 * 需要接受IdleStateEvent才能处理，必须配合IdleStateHandler使用。
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Slf4j
@Component
@Sharable
public class ServerHeartbeatHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ChannelCloser channelCloser;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            // 空闲超过事件产生是，发送心跳询问消息，若不能发送，则断开连接。
            // 因为能直接从是否发送判断连接是否有效，所以整体忽略客户端的心跳回复消息
            OutMessage outMsg = new OutMessage(new byte[] {});
            ctx.channel().writeAndFlush(outMsg).addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        log.info("A channel is idle too lang and can't be connected, will be closed!");
                        channelCloser.close(future.channel());
                    }
                }
            });
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
