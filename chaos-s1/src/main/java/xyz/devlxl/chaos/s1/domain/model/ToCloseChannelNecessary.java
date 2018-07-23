package xyz.devlxl.chaos.s1.domain.model;

import java.util.Date;

import io.netty.channel.Channel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import xyz.devlxl.chaos.base.domain.DomainEvent;

/**
 * 事件源：关闭通道是有必要的
 * 
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@Getter
@ToString
@EqualsAndHashCode
public class ToCloseChannelNecessary implements DomainEvent {
    /** 哪个通道 */
    private Channel channel;
    /** 事件发生事件 */
    private Date occurredOn;

    public ToCloseChannelNecessary(Channel channel) {
        super();
        this.channel = channel;
        this.occurredOn = new Date();
    }

    @Override
    public Date occurredOn() {
        return this.occurredOn;
    }

}
