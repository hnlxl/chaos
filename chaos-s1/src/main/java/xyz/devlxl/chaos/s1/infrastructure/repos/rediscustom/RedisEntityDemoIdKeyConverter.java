package xyz.devlxl.chaos.s1.infrastructure.repos.rediscustom;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import xyz.devlxl.chaos.s1.domain.model.RedisEntityDemoId;

/**
 * 
 * @author Liu Xiaolei
 * @date 2018/09/05
 */
@WritingConverter
public class RedisEntityDemoIdKeyConverter implements Converter<RedisEntityDemoId, String> {

    @Override
    public String convert(RedisEntityDemoId source) {
        return source.getId();
    }

}
