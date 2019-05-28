package me.ibore.libs.exception;

import me.ibore.libs.basic.XException;

public class HttpException extends XException {

    /**
     * code为-1时，是各种IOException异常
     */
    private final int code;

    public static final int IO_EXCEPTION = -1;
    public static final int UNKNOWN_HOST_EXCEPTION = IO_EXCEPTION - 1;
//    public static final int IO_EXCEPTION = -1;
//    public static final int IO_EXCEPTION = -1;

    public HttpException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return code + ":" + super.getMessage();
    }
}
