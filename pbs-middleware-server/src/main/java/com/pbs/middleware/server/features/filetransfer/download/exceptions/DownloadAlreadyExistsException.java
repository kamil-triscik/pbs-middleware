package com.pbs.middleware.server.features.filetransfer.download.exceptions;

import com.pbs.middleware.server.common.exception.AlreadyExistsException;
import java.util.UUID;

public final class DownloadAlreadyExistsException extends AlreadyExistsException {

    public DownloadAlreadyExistsException(UUID id) {
        super("Upload with id " + id + " already exists!", "id", DownloadErrorCode.DOWNLOAD__ALREADY_EXISTS);
    }

}
