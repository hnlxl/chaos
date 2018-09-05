package xyz.devlxl.chaos.s1.domain.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import xyz.devlxl.chaos.support.domain.AbstractAggregateRoot;

/**
 * The demo of entity stored to Redis
 * 
 * <p>
 * <strong>Warning: Redis transaction mechanism do not have the ACID principle, so neither the Aggregate Root nor the
 * Event Store has strong consistency. If there is a requirement for strong consistency, do not use the Redis Aggregate
 * Root. </strong>
 * 
 * @author Liu Xiaolei
 * @date 2018/09/05
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@RedisHash(value = "S1-REPO-RedisEntityDemo")
public class RedisEntityDemo extends AbstractAggregateRoot<RedisEntityDemo> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private RedisEntityDemoId id;

    private String name;

}
