package com.pbs.middleware.server.features.filetransfer.download.domain;

public enum DownloadFileState {

    UNKNOWN,
    IN_PROGRESS,
    PREPARED,
    FAILED,
    REMOVED_FROM_TEMPORARY_STORE,

}
