package xyz.devlxl.chaos.s1.channel.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import xyz.devlxl.chaos.s1.domain.model.ChannelCloseCommand;
import xyz.devlxl.chaos.s1.domain.model.ChannelCloseCommandRepository;

/**
 * 服务器端空闲事件处理器
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
public class IdleEventHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ChannelCloseCommandRepository channelCloseCommandRepository;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            // 已确认，心跳发送成功，仅表示网络链路存在，但客户端不一定还在线（断线、程序无响应、断电等等）。
            // 因此，此处要想额外判断是否还在线，必须同步等待客户端回复的心跳响应消息。
            // 这是一个pipeline上的处理器，主线程阻塞哪怕1秒都会严重影响并发量，异步线程又没必要，所以直接断开连接。
            log.info("Channel[" + ctx.channel().toString() + "] is idle, will be closed!");
            channelCloseCommandRepository.save(new ChannelCloseCommand(ctx.channel()).confirm());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
