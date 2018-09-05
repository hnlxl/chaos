package xyz.devlxl.chaos.support.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Liu Xiaolei
 * @date 2018/09/03
 */
@Configuration
public class JpaSupportConfiguration {

    @Bean("objectMapperOfJpaSupport")
    public ObjectMapper objectMapperOfJpaSupport() {
        return new ObjectMapper();
    }
}
