package xyz.devlxl.chaos.s1.domain.model;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import xyz.devlxl.chaos.support.domain.AbstractDomainEvent;

/**
 * An event indicating that the event Channel close command has been confirmed
 * 
 * @author Liu Xiaolei
 * @date 2018/09/06
 */
@AllArgsConstructor
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelCloseCommandConfirmed extends AbstractDomainEvent {
    /** which channel */
    private Channel channel;
}
