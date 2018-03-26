package dev.teclxl.chaos.nettyserver.handler;

import org.springframework.stereotype.Component;

import dev.teclxl.chaos.nettyserver.OutMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 消息编码者（按消息协议统一编码成一帧消息）
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Component
@Sharable
public class OutMessageEncoder extends MessageToByteEncoder<OutMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, OutMessage msg, ByteBuf out) throws Exception {
        out.writeBytes(new byte[] {});

    }

}
