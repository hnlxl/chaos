package xyz.devlxl.chaos.support.domainevents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Liu Xiaolei
 * @date 2018/09/11
 */
@Configuration
public class DomainEventsSupportConfiguration {

    @Autowired
    private VerifyingDomainEventsPublishedInterceptor verifyingDomainEventsPublishedInterceptor;

    @Bean("objectMapperOfJpaSupport")
    public ObjectMapper objectMapperOfJpaSupport() {
        return new ObjectMapper();
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return (hibernateProperties) -> {
            // hibernate.ejb.interceptor.session_scoped配置session级别拦截器
            // hibernate.ejb.interceptor配置全局级别拦截器
            hibernateProperties.put("hibernate.ejb.interceptor", verifyingDomainEventsPublishedInterceptor);
        };
    }
}
