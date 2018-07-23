package xyz.devlxl.chaos.s1.channel.handler;

import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import xyz.devlxl.chaos.s1.base.pojo.OutboundMessage;

/**
 * 消息编码者（按消息协议统一编码成一帧消息）
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Component
@Sharable
public class OutboundMessageEncoder extends MessageToByteEncoder<OutboundMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, OutboundMessage msg, ByteBuf out) throws Exception {
        out.writeBytes(new byte[] {});

    }

}
