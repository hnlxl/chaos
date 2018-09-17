package xyz.devlxl.chaos.support.domainevents.scsdemo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Liu Xiaolei
 * @date 2018/07/31
 */
@Data
@Accessors(chain = true)
public class Person {
    private String name;
}
