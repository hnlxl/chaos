package xyz.devlxl.chaos.s1.base.pojo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 解码后的统一入站消息，不可变对象
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode
public class InboundMessage {

    private byte[] demo;

    /**
     * 对象复制
     * 
     * @param aInboundMessage
     */
    public InboundMessage(InboundMessage aInboundMessage) {
        super();
        this.demo = aInboundMessage.getDemo();
    }

}
