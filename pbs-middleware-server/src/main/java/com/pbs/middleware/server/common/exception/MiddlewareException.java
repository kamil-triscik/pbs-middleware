package com.pbs.middleware.server.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
public abstract class MiddlewareException extends RuntimeException {

    public MiddlewareException(HttpStatus httpStatus, ErrorCode errorCode, String errorMessage) {
        this(httpStatus, errorCode.toString(), errorMessage);
    }

    public MiddlewareException(HttpStatus httpStatus, String errorCode, String errorMessage) {
        super(errorMessage);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public MiddlewareException(HttpStatus httpStatus, ErrorCode errorCode, String errorMessage, Throwable throwable) {
        this(httpStatus, errorCode.toString(), errorMessage, throwable);
    }

    public MiddlewareException(HttpStatus httpStatus, String errorCode, String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public MiddlewareException(HttpStatus httpStatus, ErrorCode errorCode, String errorMessage, String field) {
        this(httpStatus, errorCode.toString(), errorMessage, field);
    }

    public MiddlewareException(HttpStatus httpStatus, String errorCode, String errorMessage, String field) {
        super(errorMessage);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.field = field;
    }

    public MiddlewareException(String errorMessage, ErrorCode errorCode, Throwable throwable) {
        super(errorMessage, throwable);
        this.httpStatus = INTERNAL_SERVER_ERROR;
        this.errorCode = errorCode.toString();
        this.errorMessage = errorMessage;
    }

    private HttpStatus httpStatus;
    private String errorCode;
    private String errorMessage;
    private String field = null;

}
