package dev.teclxl.chaos.support.exception;

/**
 * 当业务服务执行过程中出错时，抛出的异常。这些异常可能是对具体异常的简单包装，也可能仅仅表示执行中有错误发生。
 * 
 * <p>
 * 添加了一个ServiceErrorInfo，用于提供额外的错误信息。
 * 
 * @author Liu Xiaolei
 * @date 2018/03/26
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    /** 错误类型 */
    protected final ServiceErrorInfo errorInfo;

    public ServiceException() {
        super();
        this.errorInfo = ServiceGlobalError.NO_CARE;
    }

    public ServiceException(String message) {
        super(message);
        this.errorInfo = ServiceGlobalError.NO_CARE;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.errorInfo = ServiceGlobalError.NO_CARE;
    }

    public ServiceException(Throwable cause) {
        super(cause);
        this.errorInfo = ServiceGlobalError.NO_CARE;
    }

    public ServiceException(ServiceErrorInfo errorInfo) {
        super();
        this.errorInfo = errorInfo != null ? errorInfo : ServiceGlobalError.NO_CARE;
    }

    public ServiceException(ServiceErrorInfo errorInfo, String message) {
        super(message);
        this.errorInfo = errorInfo != null ? errorInfo : ServiceGlobalError.NO_CARE;
    }

    public ServiceException(ServiceErrorInfo errorInfo, String message, Throwable cause) {
        super(message, cause);
        this.errorInfo = errorInfo != null ? errorInfo : ServiceGlobalError.NO_CARE;
    }

    public ServiceException(ServiceErrorInfo errorInfo, Throwable cause) {
        super(cause);
        this.errorInfo = errorInfo != null ? errorInfo : ServiceGlobalError.NO_CARE;
    }

    public ServiceErrorInfo getErrorInfo() {
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
     * @TODO，改成标准的getMessage方法的信息扩展方式，移除getOriginalMessage方法
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
