package com.pbs.middleware.server.features.filetransfer.upload.exceptions;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public final class UploadToServerException extends UploadException {

    public UploadToServerException(String temp, String destination, Throwable throwable) {
        super(
                INTERNAL_SERVER_ERROR,
                UploadErrorCode.UPLOAD__REMOTE_UPLOAD_FAILED,
                "Uploading file[" + temp + "] to " + destination + " failed!",
        throwable);
    }

}
