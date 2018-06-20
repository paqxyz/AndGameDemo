package cn.ollyice.library.butterknife.internal;

/**
 * Created by admin on 2018/6/4.
 */
public class ReflectException extends RuntimeException {
    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectException() {
        super();
    }

    public ReflectException(Throwable cause) {
        super(cause);
    }
}