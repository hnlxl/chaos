package xyz.devlxl.chaos.gateway;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Liu Xiaolei
 * @date 2018/07/20
 */
@EnableSwagger2
public class ChaosGatewaySwagger2Config {

    @Component
    @Primary
    class SwaggerResourcesProviderCustom implements SwaggerResourcesProvider {
        @Override
        public List<SwaggerResource> get() {
            List<SwaggerResource> resources = new ArrayList<>();
            resources.add(swaggerResource("api by service one", "/chaos-serviceone/v2/api-docs", "0.0.1"));
            return resources;
        }

        private SwaggerResource swaggerResource(String name, String url, String version) {
            SwaggerResource swaggerResource = new SwaggerResource();
            swaggerResource.setName(name);
            swaggerResource.setUrl(url);
            swaggerResource.setSwaggerVersion(version);
            return swaggerResource;
        }
    }
}
