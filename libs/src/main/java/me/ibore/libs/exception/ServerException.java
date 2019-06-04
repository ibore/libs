package me.ibore.libs.exception;

import me.ibore.libs.basic.XException;

public class ServerException extends XException {

    private final int code;

    public ServerException(int code, String message) {
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
