package com.pbs.middleware.server.features.filetransfer.download.exceptions;

import com.pbs.middleware.server.features.filetransfer.download.domain.DownloadState;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class InvalidDownloadActionException extends DownloadException {

    private InvalidDownloadActionException(DownloadErrorCode errorCode, String message) {
        super(FORBIDDEN, errorCode, message);
    }

    public static InvalidDownloadActionException invalidConfirmAction(DownloadState state) {
        return new InvalidDownloadActionException(DownloadErrorCode.DOWNLOAD__FORBIDDEN_CONFIRM_ACTION, "Confirm action is forbidden for download process in state " + state);
    }

    public static InvalidDownloadActionException invalidCancelAction(DownloadState state) {
        return new InvalidDownloadActionException(DownloadErrorCode.DOWNLOAD__FORBIDDEN_CONFIRM_ACTION, "Cancel action is forbidden for download process in state " + state);
    }

    public static InvalidDownloadActionException dataNotPrepared() {
        return new InvalidDownloadActionException(DownloadErrorCode.DOWNLOAD__FORBIDDEN_USER_DOWNLOAD_ACTION, "File download is enabled only for download process in state FETCHED");
    }
}
