package xyz.devlxl.chaos.s1;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

import xyz.devlxl.chaos.base.domain.DomainEventPublisher;
import xyz.devlxl.chaos.base.properties.ChaosCloudProperties;
import xyz.devlxl.chaos.base.properties.S1NettyServerProperties;
import xyz.devlxl.chaos.s1.resources.S1Swagger2Config;

/**
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@SpringCloudApplication
@EnableFeignClients
@EnableConfigurationProperties({S1NettyServerProperties.class, ChaosCloudProperties.class})
@Import(S1Swagger2Config.class)
@EnableJpaAuditing
public class ChaosS1Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ChaosS1Application.class)
            .listeners(new ContextRefreshedEventListener())
            .listeners(new ContextClosedEventListener())
            .run(args);
    }

    @Bean
    public DomainEventPublisher domainEventPublisher() {
        return new DomainEventPublisher();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }
}
