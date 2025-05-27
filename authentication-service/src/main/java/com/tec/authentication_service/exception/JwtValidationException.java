package com.tec.authentication_service.exception;

public class JwtValidationException extends RuntimeException{

    public final String errorCode;

    public JwtValidationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
