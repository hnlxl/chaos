package dev.teclxl.chaos.nettyserver.handler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.teclxl.chaos.nettyserver.ChannelCloser;
import dev.teclxl.chaos.nettyserver.InMessage;
import dev.teclxl.chaos.nettyserver.NettyServerProperties;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Slf4j
@Component
@Sharable
public class InMessageHandler extends SimpleChannelInboundHandler<InMessage> {

    @Autowired
    private ChannelCloser channelCloser;

    @Autowired
    private NettyServerProperties properties;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, InMessage msg) throws Exception {
        InMessageAddition msgAddition = new InMessageAddition(new Date());

        // 切面处理的正式设计前临时方式。目前的方式需要在业务方法体内部有插入代码，换成切面则需要合理的应用方法的返回参数。
        try {
            // @Begin

            // 执行业务，现象调用时传递ctx、msg、msgAddition

            // @AfterReturing
        } catch (Exception e) {
            // @AfterThrowing
            throw e;
        } finally {
            // @After
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // TODO
        log.error("", cause);
        channelCloser.close(ctx.channel());
    }

}
