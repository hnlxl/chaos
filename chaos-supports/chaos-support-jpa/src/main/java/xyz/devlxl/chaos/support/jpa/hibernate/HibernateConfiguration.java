package xyz.devlxl.chaos.support.jpa.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import xyz.devlxl.chaos.support.jpa.domain.VerifyingDomainEventsPublishedInterceptor;

/**
 * @author Liu Xiaolei
 * @date 2018/08/29
 */
@Configuration
public class HibernateConfiguration {

    @Autowired
    private VerifyingDomainEventsPublishedInterceptor verifyingDomainEventsPublishedInterceptor;

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return (hibernateProperties) -> {
            // hibernate.ejb.interceptor.session_scoped配置session级别拦截器
            // hibernate.ejb.interceptor配置全局级别拦截器
            hibernateProperties.put("hibernate.ejb.interceptor", verifyingDomainEventsPublishedInterceptor);
        };
    }
}
