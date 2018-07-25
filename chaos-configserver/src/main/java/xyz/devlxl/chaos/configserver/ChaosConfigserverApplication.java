package xyz.devlxl.chaos.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@EnableConfigServer
@SpringBootApplication
public class ChaosConfigserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChaosConfigserverApplication.class, args);
    }
}
