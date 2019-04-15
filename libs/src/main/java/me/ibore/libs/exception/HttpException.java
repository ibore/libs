package me.ibore.libs.exception;

import me.ibore.libs.base.XException;

public class HttpException extends XException {

    /**
     * code为-1时，是各种IOException异常
     */
    private final int code;

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
