package dev.teclxl.chaos.nettyserver;

import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * 通道关闭者，因为通道关闭时可能要做一些日志记录或清理工作，所以在此处统一处理。
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Slf4j
@Component
public class ChannelCloser {

    public void close(Channel channel) {
        log.info("Will close channel, channel is: " + channel.toString());
        channel.close();
    }
}
