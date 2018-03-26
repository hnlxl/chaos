package dev.teclxl.chaos.support.exception;

/**
 * ServiceErrorInfo需要向外反馈时的转换示例
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
public interface OutDemoErrorInfo {
    /**
     * 错误信息枚举的级别
     * 
     * @author Liu Xiaolei
     * @date 2018/03/26
     */
    enum Level {
        /** 通用级别 */
        GLOBAL,
        /** 接口级别 */
        INTERFACE,
        /** 方法级别 */
        METHOD
    }

    /**
     * 指示当前错误信息的级别
     * 
     * @return
     */
    Level level();

    /**
     * 指示当前具体错误信息的、未正规化的、内部使用的唯一标示。
     * 
     * 默认情况下，由以下信息构成：{错误信息的枚举常量名}-{级别的枚举常量名}-{枚举类的类名}。
     * 例如：SOME_ERROR-INTERFACE-dev.teclxl.chaos.xxx.XxxServiceOutDemo$Error
     * 
     * @return
     */
    default String unformalizedId() {
        Enum<?> enumThis = (Enum<?>)this;
        StringBuilder sb = new StringBuilder()
            .append(enumThis.name())
            .append("-")
            .append(this.level().name())
            .append("-")
            .append(enumThis.getDeclaringClass().getName());

        return sb.toString();
    }
}
