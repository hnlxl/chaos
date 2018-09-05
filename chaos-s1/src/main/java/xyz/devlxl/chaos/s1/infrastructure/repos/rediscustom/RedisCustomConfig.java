package xyz.devlxl.chaos.s1.infrastructure.repos.rediscustom;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.redis.core.convert.RedisCustomConversions;

/**
 * @author Liu Xiaolei
 * @date 2018/09/05
 */
@Configuration
public class RedisCustomConfig {
    public static final Charset STR_CHARSET = Charset.forName("UTF-8");

    @Bean
    public CustomConversions redisCustomConversions() {
        return new RedisCustomConversions(
            Arrays.asList(
                new RedisEntityDemoIdKeyConverter(),
                new RedisEntityDemoIdReadingConverter(),
                new RedisEntityDemoIdWritingConverter()));
    }
}
