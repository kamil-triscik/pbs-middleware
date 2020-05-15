package com.pbs.middleware.server.features.filetransfer.download.exceptions;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public final class DownloadFromServerException extends DownloadException {

    public DownloadFromServerException(String filename, Throwable throwable) {
        super(
                INTERNAL_SERVER_ERROR,
                DownloadErrorCode.DOWNLOAD__REMOTE_DOWNLOAD_FAILED,
                "Downloading file[" + filename + " failed!",
                throwable);
    }

}
