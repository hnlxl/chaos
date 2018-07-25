package xyz.devlxl.chaos.base.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Data
@ConfigurationProperties("s1.netty-server")
public class S1NettyServerProperties {
    /** 是否实际地启动，默认是，单元测试时需要关闭 */
    private boolean actuallyStarted = true;
    /** 端口 */
    private int port = 15001;
    /** Boss组线程数量 */
    private int bossThreadCount = 2;
    /** Worker组线程数量 */
    private int workerThreadCount = 2;
    /** 业务处理器组线程数量 */
    private int businessThreadCount = 16;
    /** SO_KEEPALIVE配置 */
    private boolean soKeepalive = true;
    /** SO_BACKLOG配置 */
    private int soBacklog = 128;
    /** 最大读空闲（秒数），0表示不控制 */
    private int maxReaderIdle = 0;
    /** 最大写空闲（秒数），0表示不控制 */
    private int maxWriterIdle = 0;
    /** 最大即没读又没写空闲（秒数），0表示不控制 */
    private int maxAllIdle = 300;
    /**
     * 验证连接是否有效的延迟时间（毫秒数）。
     * <p>
     * 说明：连接激活时依据该时间计算验证时间并放入延迟队列，一个单独的任务线程将从延迟队列中取出并验证连接是否有效（目前阶段的验证手段是是否已经绑定设备号）
     */
    private long delayOfVerifyChannelValidity = 30000;
}
