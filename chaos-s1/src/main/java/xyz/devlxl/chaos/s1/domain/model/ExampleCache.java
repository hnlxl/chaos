package xyz.devlxl.chaos.s1.domain.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 示例Redis缓存模型
 * 
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "S1-REPO-ExampleCache", timeToLive = 180)
public class ExampleCache implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String key;

    private Long field1;

    private Boolean field2;
}
