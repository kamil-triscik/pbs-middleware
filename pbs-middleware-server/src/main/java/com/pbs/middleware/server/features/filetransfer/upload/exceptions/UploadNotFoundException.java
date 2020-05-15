package com.pbs.middleware.server.features.filetransfer.upload.exceptions;

import com.pbs.middleware.server.common.exception.NotFoundException;
import java.util.UUID;

import static com.pbs.middleware.server.features.filetransfer.upload.exceptions.UploadErrorCode.UPLOAD__NOT_FOUND;

public final class UploadNotFoundException extends NotFoundException {

    public UploadNotFoundException(UUID id) {
        super("Upload with id " + id + " not found!", "id", UPLOAD__NOT_FOUND);
    }

}
