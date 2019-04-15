package me.ibore.libs.exception;

import me.ibore.libs.base.XException;

public class ClientException extends XException {

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
