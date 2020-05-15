package com.pbs.middleware.server.features.filetransfer.download.domain;

public enum DownloadState {

    /**
     * Unknown state.
     */
    UNKNOWN,
    /**
     * Download request received na accepted.
     */
    REQUESTED,
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
     * User confirmed that all fetched files from temporary storage were downloaded and download process is finished.
     */
    DOWNLOADED,
    /**
     * Post process actions(e.g. dropping temp files) are in progress.
     */
    POST_PROCESSING,
    /**
     * All post process actions finished.
     */
    FINISHED,
    /**
     * Post processing failed.
     */
    FINISHED_WITH_WARNING,
    /**
     * Download was cancelled.
     */
    CANCELED,
    /**
     * Download failed for some reason.
     */
    FAILED;

    public static String getState(DownloadState state) {
        return switch (state) {
            case UNKNOWN, REQUESTED, IN_PROGRESS -> IN_PROGRESS.toString();
            case READY -> READY.toString();
            case DOWNLOADED, POST_PROCESSING, FINISHED, FINISHED_WITH_WARNING, CANCELED -> FINISHED.toString();
            case FAILED -> FAILED.toString();
            default -> throw new IllegalArgumentException("Unknown download state");
        };
    }

}
