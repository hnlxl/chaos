package dev.teclxl.chaos.support.exception;

/**
 * ServiceErrorInfo需要向外反馈时的异常示例
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
public class OutDemoException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    /** 额外的错误信息 */
    protected final OutDemoErrorInfo errorInfo;

    public OutDemoException() {
        super();
        this.errorInfo = OutDemoGlobalError.NO_CARE;
    }

    public OutDemoException(String message) {
        super(message);
        this.errorInfo = OutDemoGlobalError.NO_CARE;
    }

    public OutDemoException(String message, Throwable cause) {
        super(message, cause);
        this.errorInfo = OutDemoGlobalError.NO_CARE;
    }

    public OutDemoException(Throwable cause) {
        super(cause);
        this.errorInfo = OutDemoGlobalError.NO_CARE;
    }

    public OutDemoException(OutDemoErrorInfo errorInfo) {
        super();
        this.errorInfo = errorInfo != null ? errorInfo : OutDemoGlobalError.NO_CARE;
    }

    public OutDemoException(OutDemoErrorInfo errorInfo, String message) {
        super(message);
        this.errorInfo = errorInfo != null ? errorInfo : OutDemoGlobalError.NO_CARE;
    }

    public OutDemoException(OutDemoErrorInfo errorInfo, String message, Throwable cause) {
        super(message, cause);
        this.errorInfo = errorInfo != null ? errorInfo : OutDemoGlobalError.NO_CARE;
    }

    public OutDemoException(OutDemoErrorInfo errorInfo, Throwable cause) {
        super(cause);
        this.errorInfo = errorInfo != null ? errorInfo : OutDemoGlobalError.NO_CARE;
    }

    public OutDemoErrorInfo getErrorInfo() {
        return errorInfo;
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
     * 在原方法上扩展，增加对errorInfo信息的返回
     * 
     * @TODO 同ServiceException
     */
    @Override
    public String getMessage() {
        StringBuilder newMessage = new StringBuilder()
            .append("errorInfo : ")
            .append(errorInfo.unformalizedId())
            .append(", ");
        if (getOriginalMessage() == null) {
            newMessage.append("original message is null");
        } else {
            newMessage.append("original message is : ")
                .append(getOriginalMessage());
        }
        return newMessage.toString();
    }
}
