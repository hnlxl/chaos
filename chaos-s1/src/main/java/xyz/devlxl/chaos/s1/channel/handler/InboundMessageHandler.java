package xyz.devlxl.chaos.s1.channel.handler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import xyz.devlxl.chaos.base.properties.S1NettyServerProperties;
import xyz.devlxl.chaos.s1.base.pojo.InboundMessage;
import xyz.devlxl.chaos.s1.base.pojo.InboundMessageAddition;
import xyz.devlxl.chaos.s1.domain.model.ChannelCloseCommand;
import xyz.devlxl.chaos.s1.domain.model.ChannelCloseCommandRepository;

/**
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Slf4j
@Component
@Sharable
public class InboundMessageHandler extends SimpleChannelInboundHandler<InboundMessage> {

    @Autowired
    private ChannelCloseCommandRepository channelCloseCommandRepository;

    @Autowired
    private S1NettyServerProperties properties;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, InboundMessage msg) throws Exception {
        InboundMessageAddition msgAddition = new InboundMessageAddition(new Date());

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
        channelCloseCommandRepository.save(new ChannelCloseCommand(ctx.channel()).confirm());
    }

}
