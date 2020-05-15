package com.pbs.middleware.server.features.filetransfer.download.exceptions;

import com.pbs.middleware.server.common.exception.ErrorCode;

public enum DownloadErrorCode implements ErrorCode {

    DOWNLOAD__ALREADY_EXISTS("Download with provided id already exist."),
    DOWNLOAD__NOT_FOUND("Job with provided id does not exists."),
    DOWNLOAD__LOADING_EXCEPTION("Job loading from DB problem."),

    DOWNLOAD__TEMP_FILE_NOT_FOUND("File not found on temporary storage"),
    DOWNLOAD__TEMP_FILE_LOAD_FAILED("File not found on temporary storage"),

    DOWNLOAD__CONNECTION_NOT_FOUND("File not found on temporary storage"),
    DOWNLOAD__INVALID_EMAIL("File not found on temporary storage"),
    DOWNLOAD__INVALID_DOWNLOAD_TARGET("File not found on temporary storage"),

    DOWNLOAD__REMOTE_DOWNLOAD_FAILED("File not found on temporary storage"),

    DOWNLOAD__FORBIDDEN_CONFIRM_ACTION("File not found on temporary storage"),
    DOWNLOAD__FORBIDDEN_CANCEL_ACTION("File not found on temporary storage"),
    DOWNLOAD__FORBIDDEN_USER_DOWNLOAD_ACTION("File not found on temporary storage");

    public final String description;


    DownloadErrorCode(final String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
