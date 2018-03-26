package dev.teclxl.chaos.nettyserver.handler;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.teclxl.chaos.nettyserver.InMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * 入站消息解码者（将一帧消息按协议统一解码）。 <strong>在该处理之前，必须已完成粘包。即该处理在通道中应该处于FrameDecoder之后</strong>
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Component
@Sharable
public class InMessageDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

        InMessage decodedMsg = new InMessage(new byte[] {});
        out.add(decodedMsg);
    }

}
