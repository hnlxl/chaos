package xyz.devlxl.chaos.s1.application.subscriber;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import xyz.devlxl.chaos.base.domain.DomainEventPublisher;
import xyz.devlxl.chaos.base.domain.DomainEventSubscriber;
import xyz.devlxl.chaos.s1.domain.model.ToCloseChannelNecessary;

/**
 * ToCloseChannelNecessary订阅者
 * 
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@Service
@Slf4j
public class ToCloseChannelNecessarySubscriber implements DomainEventSubscriber<ToCloseChannelNecessary> {
    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @PostConstruct
    public void subscribe() {
        domainEventPublisher.register(ToCloseChannelNecessary.class, this);
    }

    @Override
    public void handle(ToCloseChannelNecessary event) {
        Channel channel = event.getChannel();
        log.info("Will close channel, channel is: " + channel.toString());
        channel.close();
    }
}
