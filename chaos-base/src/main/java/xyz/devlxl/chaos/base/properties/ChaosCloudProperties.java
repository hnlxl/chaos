package xyz.devlxl.chaos.base.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 微服务整体云级别的配置项
 * 
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@ConfigurationProperties("chaos-cloud")
@Data
public class ChaosCloudProperties {
    // 这个配置是给Swagger用的，业务中用不到。
    /** 网关主机 */
    private String gatewayHost = "gateway.chaos.devlxl.xyz:10003";
}
