package com.pbs.middleware.api.upload;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "FileUploadState", description = "Enumeration of available uploaded file states.")
public enum FileUploadState {

    /**
     * FPS does not have file in temporary storage. Client didn't provided file yet.
     */
    NOT_PROVIDED,
    /**
     * File is being uploaded.
     */
    IN_PROGRESS,
    /**
     *
     */
    UPLOADED,
    /**
     * File upload failed.
     */
    FAILED,
    /**
     * File upload was cancelled.
     */
    CANCELLED,
    /**
     * Unknown file state.
     */
    UNKNOWN

}
