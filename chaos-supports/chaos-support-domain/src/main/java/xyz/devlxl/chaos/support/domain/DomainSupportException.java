package xyz.devlxl.chaos.support.domain;

/**
 * Thrown to indicate that domain support is not working properly.
 * 
 * @author Liu Xiaolei
 * @date 2018/09/05
 */
public class DomainSupportException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DomainSupportException() {}

    public DomainSupportException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DomainSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainSupportException(String message) {
        super(message);
    }

    public DomainSupportException(Throwable cause) {
        super(cause);
    }
}
