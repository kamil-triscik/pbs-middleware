package com.pbs.middleware.server.features.filetransfer.download.exceptions;

import com.pbs.middleware.server.common.exception.MiddlewareException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class UserFileDownloadException extends MiddlewareException {

    public UserFileDownloadException(String message) {
        super(
                INTERNAL_SERVER_ERROR,
                DownloadErrorCode.DOWNLOAD__TEMP_FILE_LOAD_FAILED,
                "Files downloading failed:" + message);
    }
}
