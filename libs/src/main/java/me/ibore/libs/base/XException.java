package me.ibore.libs.base;

public class XException extends RuntimeException {

    public XException() {
    }

    public XException(String message) {
        super(message);
    }

    public XException(String message, Throwable cause) {
        super(message, cause);
    }

    public XException(Throwable cause) {
        super(cause);
    }


}
