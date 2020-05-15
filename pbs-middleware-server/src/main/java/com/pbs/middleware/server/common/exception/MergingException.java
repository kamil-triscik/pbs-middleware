package com.pbs.middleware.server.common.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public final class MergingException extends MiddlewareException {

    public MergingException(String message) {
        super(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getReasonPhrase(), message);
    }

}
