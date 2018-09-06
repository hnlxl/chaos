package xyz.devlxl.chaos.s1.application.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import xyz.devlxl.chaos.s1.domain.model.ChannelCloseCommandConfirmed;

/**
 * @author Liu Xiaolei
 * @date 2018/09/06
 */
@Slf4j
@Service
public class ChannelCloseCommandListeners {
    @EventListener
    @Async
    public void listenChannelCloseCommandConfirmed(ChannelCloseCommandConfirmed event) {
        Channel channel = event.getChannel();
        log.info("Will close channel, channel is: " + channel.toString());
        channel.close();
    }
}
