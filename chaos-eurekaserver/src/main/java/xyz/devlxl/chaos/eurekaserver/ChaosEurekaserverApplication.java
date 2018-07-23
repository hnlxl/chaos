package xyz.devlxl.chaos.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@EnableEurekaServer
@SpringBootApplication
public class ChaosEurekaserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChaosEurekaserverApplication.class, args);
    }
}
