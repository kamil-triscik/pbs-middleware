package com.pbs.middleware.server.common.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class NotFoundException extends MiddlewareException {

    public NotFoundException(String errorMessage, String field, ErrorCode errorCode) {
        super(NOT_FOUND, errorCode, errorMessage, field);
    }

}
