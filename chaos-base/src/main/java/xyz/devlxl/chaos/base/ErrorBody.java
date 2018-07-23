package xyz.devlxl.chaos.base;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微服务异常返回时的，统一的消息体
 * <p>
 * <strong>仅当HTTP状态码不是2**时使用该类，2**时将只包含有意义的数据，不包装成统一类型</strong>
 * 
 * @author Liu Xiaolei
 * @date 2018/07/21
 */
@Data
@Accessors(chain = true)
public class ErrorBody<T> {
    /** 通过Request Attribute传递数据时的属性键，message数据的键 */
    public static final String ATTR_KEY_MSG = "ERROR/MSG";
    /** 通过Request Attribute传递数据时的属性键，data数据的键 */
    public static final String ATTR_KEY_DATA = "ERROR/DATA";

    /** 生成错误返回时的时间戳 */
    private Date timestamp;
    /** HTTP状态码 */
    private Integer httpStatus;
    /** HTTP错误说明 */
    private String httpError;
    /**
     * 错误代码
     * 
     * @see ErrorCodeEnumerable
     */
    private String errorCode;
    /** 可选附加的错误消息 */
    private String message = null;
    /** 可选附加的数据 */
    private T data = null;
    /**
     * 外部接口返回的错误，只在调用外部接口并且接口没有正常返回时设置
     */
    private Object errorByApi = null;

    /**
     * 转换成MAP，将移除所有值为null的字段
     * 
     * @return 根据该实体生成的“成员变量-值”关系图，不包含值为null的成员
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();

        if (timestamp != null) {
            map.put("timestamp", timestamp);
        }
        if (httpStatus != null) {
            map.put("httpStatus", httpStatus);
        }
        if (httpError != null) {
            map.put("httpError", httpError);
        }
        if (errorCode != null) {
            map.put("errorCode", errorCode);
        }
        if (message != null) {
            map.put("message", message);
        }
        if (data != null) {
            map.put("data", data);
        }
        if (errorByApi != null) {
            map.put("errorByApi", errorByApi);
        }

        return map;
    }
}