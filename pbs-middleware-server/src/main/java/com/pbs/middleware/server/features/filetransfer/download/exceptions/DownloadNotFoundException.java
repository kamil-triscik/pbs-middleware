package com.pbs.middleware.server.features.filetransfer.download.exceptions;

import com.pbs.middleware.server.common.exception.NotFoundException;
import java.util.UUID;

public final class DownloadNotFoundException extends NotFoundException {

    public DownloadNotFoundException(UUID id) {
        super("Download with id " + id + " not found!", "id", DownloadErrorCode.DOWNLOAD__NOT_FOUND);
    }

}
