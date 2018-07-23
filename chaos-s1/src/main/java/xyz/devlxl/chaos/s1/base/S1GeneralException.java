package xyz.devlxl.chaos.s1.base;

import xyz.devlxl.chaos.base.ErrorCodeEnumerable;

/**
 * 服务内通用异常。以ErrorCodeEnumerable字段指示是哪种错误，可以从异常直接提取错误代码。
 * 
 * TODO 这样省事但其实不是异常
 * 
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
public class S1GeneralException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    /** 错误代码 */
    protected final ErrorCodeEnumerable errorCode;

    public S1GeneralException(ErrorCodeEnumerable errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public S1GeneralException(ErrorCodeEnumerable errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public S1GeneralException(ErrorCodeEnumerable errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public S1GeneralException(ErrorCodeEnumerable errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ErrorCodeEnumerable getErrorCode() {
        return errorCode;
    }

    /**
     * 返回异常的原始消息
     * 
     * @return
     */
    public String getOriginalMessage() {
        return super.getMessage();
    }

    /**
     * 在原方法上扩展，增加对errorCode的返回
     */
    @Override
    public String getMessage() {
        if (getOriginalMessage() == null && getErrorCode() == null) {
            return null;
        }

        StringBuilder newMessage = new StringBuilder();
        if (getErrorCode() != null) {
            newMessage.append("[").append(getErrorCode().toStandardString()).append("]");
        }
        if (getOriginalMessage() != null) {
            newMessage.append(getOriginalMessage());
        }
        return newMessage.toString();
    }
}
