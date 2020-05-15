package com.pbs.middleware.server.features.filetransfer.download.exceptions;

public class InvalidDownloadTargetException extends WrongDownloadRequestException {

    public InvalidDownloadTargetException(String message) {
        super(message, DownloadErrorCode.DOWNLOAD__INVALID_DOWNLOAD_TARGET);
    }

}
