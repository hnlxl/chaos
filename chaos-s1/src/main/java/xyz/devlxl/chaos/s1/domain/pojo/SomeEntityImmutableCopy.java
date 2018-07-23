package xyz.devlxl.chaos.s1.domain.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 某个实体的不可变复制对象。作用是系统上下文属性的POJO的示例
 * 
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode
public class SomeEntityImmutableCopy implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String exampleId;

    protected Boolean checked;

    protected Date createOn;

    public static SomeEntityImmutableCopy from(Object targetObj) {
        SomeEntityImmutableCopy obj = new SomeEntityImmutableCopy();

        // obj.exampleId = targetObj.getExampleId();
        // ...

        return obj;
    }

    public boolean equalsTo(Object targetObj) {
        return false;
        // return this.exampleId.equals(targetObj.getExampleId())
        // && this.checked.equals(targetObj.getChecked())
        // && ...;
    }
}
