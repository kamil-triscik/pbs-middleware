package com.pbs.middleware.server.features.filetransfer.download.exceptions;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import org.springframework.http.HttpStatus;

abstract public class DownloadException extends MiddlewareException {

    public DownloadException(HttpStatus httpStatus, DownloadErrorCode errorCode, String message) {
        super(httpStatus, errorCode, message);
    }

    public DownloadException(HttpStatus httpStatus, DownloadErrorCode errorCode, String message, Throwable throwable) {
        super(httpStatus, errorCode, message, throwable);
    }

    public DownloadException(String message, DownloadErrorCode errorCode, Throwable throwable) {
        super(message, errorCode, throwable);
    }

}
