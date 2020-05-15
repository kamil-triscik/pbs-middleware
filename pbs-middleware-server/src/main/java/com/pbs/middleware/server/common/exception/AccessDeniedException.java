package com.pbs.middleware.server.common.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class AccessDeniedException extends MiddlewareException {

    public AccessDeniedException(String errorMessage) {
        super(FORBIDDEN, "ACCESS_DENIED", errorMessage);
    }
}
