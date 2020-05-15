package com.pbs.middleware.api.upload;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "UploadState", description = "Enumeration of available upload states.")
public enum UploadState {

    /**
     * Files are in temporary storage and they are waiting to be uploaded.
     */
    QUEUED,
    /**
     * Files are boeing uploaded.
     */
    IN_PROGRESS,
    /**
     * Files are uploaded.
     */
    UPLOADED,
    /**
     * Upload failed for some reason.
     */
    FAILED,
    /**
     * Upload was cancelled by client.
     */
    CANCELLED,
    /**
     * Unknown upload state.
     */
    UNKNOWN

}
