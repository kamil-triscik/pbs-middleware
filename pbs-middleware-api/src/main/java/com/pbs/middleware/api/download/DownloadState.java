package com.pbs.middleware.api.download;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "DownloadState", description = "Enumeration of available download statuses.")
public enum DownloadState {

    /**
     * Files fetching from server to temporary storage is in progress.
     *
     */
    IN_PROGRESS,
    /**
     * All files fetched from server and stored in temporary storage. Ready for download by client.
     */
    READY,
    /**
     * All post process actions finished.
     */
    FINISHED,
    /**
     * Download was cancelled.
     */
    CANCELED,
    /**
     * Download failed for some reason.
     */
    FAILED,
    /**
     * Unknown state
     */
    UNKNOWN

}
