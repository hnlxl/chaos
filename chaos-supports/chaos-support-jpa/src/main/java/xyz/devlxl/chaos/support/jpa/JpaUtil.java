package xyz.devlxl.chaos.support.jpa;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
public final class JpaUtil {
    /**
     * 生成一个分页请求，指定分页查询截取后的元素是“首先看到的一些元素”。相当于：不分页查询，但只返回开始的一段元素。
     * 
     * @param sort
     * @return
     */
    public static PageRequest pageRequestOfFirstElements(Sort sort) {
        int notBadSize = 100;
        return PageRequest.of(0, notBadSize, sort);
    }
}
