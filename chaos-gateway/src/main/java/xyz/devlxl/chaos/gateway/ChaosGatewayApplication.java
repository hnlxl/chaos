package xyz.devlxl.chaos.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Import;

/**
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@EnableZuulProxy
@SpringCloudApplication
@Import(ChaosGatewaySwagger2Config.class)
public class ChaosGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChaosGatewayApplication.class, args);
    }
}
