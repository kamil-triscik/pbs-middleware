package com.pbs.middleware.server.features.filetransfer.upload.exceptions;

import com.pbs.middleware.server.common.exception.MiddlewareException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public abstract class WrongUploadRequestException extends MiddlewareException {

    public WrongUploadRequestException(String message, UploadErrorCode errorCode) {
        super(BAD_REQUEST, errorCode, message);
    }

    public WrongUploadRequestException(String message, String field, UploadErrorCode errorCode) {
        super(BAD_REQUEST, errorCode, message, field);
    }

}
