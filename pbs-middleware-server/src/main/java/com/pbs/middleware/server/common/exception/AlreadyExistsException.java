package com.pbs.middleware.server.common.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

abstract public class AlreadyExistsException extends MiddlewareException {

    public AlreadyExistsException(String errorMessage, String field, ErrorCode errorCode) {
        super(CONFLICT, errorCode.toString(), errorMessage, field);
    }
}
