package xyz.devlxl.chaos.support.domain;

/**
 * Thrown to indicate that work related to domain event store cannot be performed normally
 * 
 * @author Liu Xiaolei
 * @date 2018/09/05
 */
public class DomainEventStoreException extends DomainSupportException {
    private static final long serialVersionUID = 1L;

    public DomainEventStoreException() {}

    public DomainEventStoreException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DomainEventStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainEventStoreException(String message) {
        super(message);
    }

    public DomainEventStoreException(Throwable cause) {
        super(cause);
    }

}
