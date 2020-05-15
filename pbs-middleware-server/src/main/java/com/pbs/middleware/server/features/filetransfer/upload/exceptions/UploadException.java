package com.pbs.middleware.server.features.filetransfer.upload.exceptions;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import org.springframework.http.HttpStatus;

public class UploadException extends MiddlewareException {

    public UploadException(HttpStatus httpStatus, UploadErrorCode errorCode, String message) {
        super(httpStatus, errorCode, message);
    }

    public UploadException(HttpStatus httpStatus, UploadErrorCode errorCode, String message, Throwable throwable) {
        super(httpStatus, errorCode, message, throwable);
    }

    public UploadException(String message, UploadErrorCode errorCode, Throwable throwable) {
        super(message, errorCode, throwable);
    }
}
