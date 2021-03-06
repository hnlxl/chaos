package xyz.devlxl.chaos.s1.base.pojo;

import java.util.Date;

import lombok.Data;

/**
 * 入站消息的附加信息。部分可变
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@Data
public class InboundMessageAddition {
    /**
     * 消息的接受时间，具体对应的时间点是InboundMessageHandler开始介入的时间。
     */
    private final Date acceptOn;

    /**
     * 处理中的入站消息日志
     */
    private String demo = null;
}
