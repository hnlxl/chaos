package xyz.devlxl.chaos.s1.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;

import io.netty.channel.Channel;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.devlxl.chaos.support.domain.AbstractAggregateRoot;

/**
 * The command to close the channel.
 * <p>
 * Currently this model is only used to publish events for closing a channel, and its aggregates or events are not
 * intended to be stored. In addition, its events are only consumed locally and cannot be persisted. So there is no
 * strong consistency requirement. Therefore, this model uses the Redis Repository and only has one record.
 * 
 * @author Liu Xiaolei
 * @date 2018/09/06
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@RedisHash(value = "S1-REPO-CCC")
public class ChannelCloseCommand extends AbstractAggregateRoot<ChannelCloseCommand> {
    private @Id String id = "CCC";
    private transient @Transient Channel channel;

    public ChannelCloseCommand(Channel channel) {
        super();
        this.channel = channel;
    }

    public ChannelCloseCommand confirm() {
        registerEvent(new ChannelCloseCommandConfirmed(this.channel));
        return this;
    }
}
