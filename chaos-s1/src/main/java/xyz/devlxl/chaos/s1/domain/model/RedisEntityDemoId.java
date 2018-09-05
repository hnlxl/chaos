package xyz.devlxl.chaos.s1.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Liu Xiaolei
 * @date 2018/09/05
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode
public class RedisEntityDemoId {
    private String id;

    public RedisEntityDemoId(RedisEntityDemoId aRedisEntityDemoId) {
        super();
        this.id = aRedisEntityDemoId.getId();
    }
}
