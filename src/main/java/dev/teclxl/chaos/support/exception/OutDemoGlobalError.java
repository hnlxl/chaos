package dev.teclxl.chaos.support.exception;

/**
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
public enum OutDemoGlobalError implements OutDemoErrorInfo {
    /** 不关心（只是不关心专用的错误信息，如果在异常中则仍然有标准异常消息），默认值 */
    NO_CARE;

    @Override
    public Level level() {
        return OutDemoErrorInfo.Level.GLOBAL;
    }
}
