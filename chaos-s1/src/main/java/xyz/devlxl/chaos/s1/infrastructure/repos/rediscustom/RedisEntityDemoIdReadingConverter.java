package xyz.devlxl.chaos.s1.infrastructure.repos.rediscustom;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import xyz.devlxl.chaos.s1.domain.model.RedisEntityDemoId;

/**
 * @author Liu Xiaolei
 * @date 2018/09/05
 */
@ReadingConverter
public class RedisEntityDemoIdReadingConverter implements Converter<byte[], RedisEntityDemoId> {

    @Override
    public RedisEntityDemoId convert(byte[] source) {
        return new RedisEntityDemoId(new String(source, RedisCustomConfig.STR_CHARSET));
    }

}
