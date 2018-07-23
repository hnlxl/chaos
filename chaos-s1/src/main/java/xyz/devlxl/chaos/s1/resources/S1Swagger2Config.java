package xyz.devlxl.chaos.s1.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import xyz.devlxl.chaos.base.ErrorBody;
import xyz.devlxl.chaos.base.properties.ChaosCloudProperties;

/**
 * 
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@EnableSwagger2
public class S1Swagger2Config {
    @Autowired
    private TypeResolver typeResolver;
    @Autowired
    private ChaosCloudProperties chaosCloudProperties;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
            .paths(PathSelectors.any())
            .build()
            .globalOperationParameters(globalOperationParameters())
            .globalResponseMessage(RequestMethod.GET, globalResponseMessage())
            .globalResponseMessage(RequestMethod.POST, globalResponseMessage())
            .globalResponseMessage(RequestMethod.PUT, globalResponseMessage())
            .globalResponseMessage(RequestMethod.PATCH, globalResponseMessage())
            .globalResponseMessage(RequestMethod.DELETE, globalResponseMessage())
            .produces(Sets.newHashSet("application/json"))
            .additionalModels(typeResolver.resolve(ErrorBody.class))
            .host(chaosCloudProperties.getGatewayHost())
            .tags(new Tag("/demo1", "demo1"),
                new Tag("/demo2", "demo2"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("由S1服务提供的接口")
            .version("0.0.1")
            .build();
    }

    private List<Parameter> globalOperationParameters() {
        List<Parameter> list = new ArrayList<Parameter>();
        return list;
    }

    private List<ResponseMessage> globalResponseMessage() {
        List<ResponseMessage> messages = new ArrayList<>();
        ResponseMessageBuilder messageBuilder = new ResponseMessageBuilder();
        messages.add(messageBuilder.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message("Internal Server Error")
            .responseModel(new ModelRef("ErrorBody")).build());
        return messages;
    }
}
