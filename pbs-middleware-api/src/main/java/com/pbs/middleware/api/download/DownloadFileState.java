package com.pbs.middleware.api.download;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "DownloadFileState", description = "Enumeration of available download file statuses.")
public enum DownloadFileState {

    /**
     * Unknown file status
     */
    UNKNOWN,
    /**
     * File is being downloaded to temporary storage.
     */
    IN_PROGRESS,
    /**
     * File is prepared in temporary storage.
     */
    PREPARED,
    /**
     * Unable to download file.
     */
    FAILED,
    /**
     * Files was downloaded or download was cancelled.
     */
    FINISHED,

}
