package xyz.devlxl.chaos.base;

/**
 * @author Liu Xiaolei
 * @date 2018/09/05
 */
public class JsonSerializationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JsonSerializationException() {}

    public JsonSerializationException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public JsonSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonSerializationException(String message) {
        super(message);
    }

    public JsonSerializationException(Throwable cause) {
        super(cause);
    }
}
