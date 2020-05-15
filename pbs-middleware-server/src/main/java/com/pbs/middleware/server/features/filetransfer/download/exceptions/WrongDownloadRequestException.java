package com.pbs.middleware.server.features.filetransfer.download.exceptions;


import com.pbs.middleware.server.common.exception.MiddlewareException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public abstract class WrongDownloadRequestException extends MiddlewareException {

    public WrongDownloadRequestException(String message, DownloadErrorCode errorCode) {
        super(BAD_REQUEST, errorCode, message);
    }

    public WrongDownloadRequestException(String message, String field, DownloadErrorCode errorCode) {
        super(BAD_REQUEST, errorCode, message, field);
    }

}
