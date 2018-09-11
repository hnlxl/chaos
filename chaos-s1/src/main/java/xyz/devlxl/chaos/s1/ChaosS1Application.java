package xyz.devlxl.chaos.s1;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import xyz.devlxl.chaos.base.properties.ChaosCloudProperties;
import xyz.devlxl.chaos.base.properties.S1NettyServerProperties;
import xyz.devlxl.chaos.s1.resources.S1Swagger2Config;
import xyz.devlxl.chaos.support.domainevents.DomainEventsSupportConfiguration;

/**
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@SpringCloudApplication
@ComponentScan(basePackageClasses = {ChaosS1Application.class, DomainEventsSupportConfiguration.class})
@EnableFeignClients
@EnableConfigurationProperties({S1NettyServerProperties.class, ChaosCloudProperties.class})
@Import(S1Swagger2Config.class)
@EntityScan(basePackageClasses = {ChaosS1Application.class, DomainEventsSupportConfiguration.class})
@EnableJpaRepositories(basePackageClasses = {ChaosS1Application.class, DomainEventsSupportConfiguration.class})
@EnableJpaAuditing
@EnableAsync
public class ChaosS1Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ChaosS1Application.class)
            .listeners(new ApplicationStartedListener())
            .listeners(new ApplicationShutedListener())
            .run(args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }
}
