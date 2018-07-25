package xyz.devlxl.chaos.s1.resources;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import xyz.devlxl.chaos.base.properties.ChaosCloudProperties;
import xyz.devlxl.chaos.base.properties.S1NettyServerProperties;
import xyz.devlxl.chaos.s1.ApplicationContextProvider;

/**
 * @author Liu Xiaolei
 * @date 2018/07/25
 */
@Api(tags = {"/demo"})
@RestController
@RequestMapping("/demo")
@Slf4j
public class DemoController {

    @Autowired
    private S1NettyServerProperties s1NettyServerProperties;
    @Autowired
    private ChaosCloudProperties chaosCloudProperties;
    @Value("${spring.redis.database}")
    private Integer redisDatabaseNum;
    @Autowired
    private ApplicationContextProvider applicationContextProvider;

    @ApiOperation("Config Test")
    @ApiResponses({
        @ApiResponse(code = 200, message = "demo config output")
    })
    @GetMapping("/config-test")
    public Map<String, Object> configTest() {
        Map<String, Object> result = Maps.newHashMap();
        result.put("s1NettyServerProperties", s1NettyServerProperties);
        result.put("chaosCloudProperties", chaosCloudProperties);
        result.put("spring.redis.database", redisDatabaseNum);

        Environment env = applicationContextProvider.getContext().getEnvironment();
        result.put("spring.datasource.password", env.getProperty("spring.datasource.password"));
        result.put("spring.redis.password", env.getProperty("spring.redis.password"));
        return result;
    }

    @ApiOperation("All Demos")
    @ApiResponses({
        @ApiResponse(code = 200, message = "demo list")
    })
    @GetMapping
    public List<DemoVo> demos(String input) {
        return Lists.newArrayList(
            new DemoVo().setField1(input).setField2(0).setField3(input).setField4(input),
            new DemoVo().setField1("Vo2.field1").setField2(2).setField3("Vo2.field3").setField4("Vo2.field4"),
            new DemoVo().setField1("Vo3.field1").setField2(3).setField3("Vo3.field3").setField4("Vo3.field4"));
    }

    @ApiOperation("Demo by id")
    @ApiResponses({
        @ApiResponse(code = 200, message = "one demo")
    })
    @GetMapping("/{demoId}")
    public DemoVo demoById(@PathVariable String demoId) {
        return new DemoVo().setField1("For demoId: " + demoId);
    }

    @ApiOperation("Demo new/create")
    @PostMapping
    public void newDemo(
        @ApiParam(value = "field one", required = true) @RequestParam String field1,
        @ApiParam(value = "field two", required = true,
            allowableValues = "1,2,3") @RequestParam Integer field2,
        @RequestParam String field3,
        @RequestParam(defaultValue = "666") String field4) {
        log.info("DemoController Post: " + field1 + "|" + field2 + "|" + field3 + "|" + field4);
    }

    @ApiOperation("Demo update")
    @PutMapping("/{demoId}")
    public void updateDemo(@PathVariable String demoId,
        @RequestParam String field1,
        @ApiParam(required = true, allowableValues = "1,2,3") @RequestParam Integer field2,
        @RequestParam String field3,
        @RequestParam(defaultValue = "666") String field4) {
        log.info("DemoController Put: " + demoId + "**" + field1 + "|" + field2 + "|" + field3 + "|" + field4);
    }

    @ApiOperation("Demo update dynamically")
    @PatchMapping("/{demoId}")
    public void updateDemoDynamically(@PathVariable String demoId,
        String field1,
        @ApiParam(allowableValues = "1,2,3") @RequestParam(required = false) Integer field2,
        String field3,
        String field4) {
        log.info("DemoController Patch: " + demoId + "**" + field1 + "|" + field2 + "|" + field3 + "|" + field4);
    }

    @ApiOperation("Demo delete")
    @DeleteMapping("/{demoId}")
    public void deleteDemo(@PathVariable String demoId) {
        log.info("DemoController Delete: " + demoId);
    }

    @ApiModel
    @Data
    @Accessors(chain = true)
    public static class DemoVo {
        String field1;
        Integer field2;
        String field3;
        String field4;
    }
}
