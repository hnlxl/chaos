package dev.teclxl.chaos.nettyserver;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 解码后的统一输入消息,不可变对象
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode
public class InMessage {

    private byte[] demo;

    /**
     * 对象复制
     * 
     * @param aInMessage
     */
    public InMessage(InMessage aInMessage) {
        super();
        this.demo = aInMessage.getDemo();
    }

}
