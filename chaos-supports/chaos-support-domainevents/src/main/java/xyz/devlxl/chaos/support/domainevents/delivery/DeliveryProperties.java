package xyz.devlxl.chaos.support.domainevents.delivery;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author Liu Xiaolei
 * @date 2018/09/12
 */
@ConfigurationProperties("domainevents-delivery")
@Data
public class DeliveryProperties {
    private HeaderKey headerKey = new HeaderKey();

    @Data
    public static class HeaderKey {
        private String eventType = "etype";
        private String eventId = "eid";
        private String occurredOn = "eon";
    }
}
