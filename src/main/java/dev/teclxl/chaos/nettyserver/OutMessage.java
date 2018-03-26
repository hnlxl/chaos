package dev.teclxl.chaos.nettyserver;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 编码前的统一输出消息,不可变对象
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode
public class OutMessage {

    private byte[] demo;

    /**
     * 对象复制
     * 
     * @param aOutMessage
     */
    public OutMessage(OutMessage aOutMessage) {
        super();
        this.demo = aOutMessage.getDemo();
    }

}
