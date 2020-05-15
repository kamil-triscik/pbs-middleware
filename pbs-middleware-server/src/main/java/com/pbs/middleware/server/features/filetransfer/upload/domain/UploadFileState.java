package com.pbs.middleware.server.features.filetransfer.upload.domain;

import com.pbs.middleware.api.upload.FileUploadState;

public enum UploadFileState {

    NOT_PROVIDED,
    PROVIDED,
    UPLOADING,
    UPLOADED,
    FAILED,
    REMOVED_FROM_TEMPORARY_STORAGE,
    CANCELLED;

    public static FileUploadState get(UploadFileState state) {
        return switch (state) {
            case NOT_PROVIDED -> FileUploadState.NOT_PROVIDED;
            case PROVIDED, UPLOADING -> FileUploadState.IN_PROGRESS;
            case REMOVED_FROM_TEMPORARY_STORAGE, UPLOADED -> FileUploadState.UPLOADED;
            case FAILED -> FileUploadState.FAILED;
            default -> FileUploadState.UNKNOWN;
        };
    }

}
