package com.pbs.middleware.server.features.filetransfer.upload.exceptions;

import com.pbs.middleware.server.common.exception.AlreadyExistsException;
import java.util.UUID;

import static com.pbs.middleware.server.features.filetransfer.upload.exceptions.UploadErrorCode.UPLOAD__ALREADY_EXISTS;

public final class UploadAlreadyExistsException extends AlreadyExistsException {

    public UploadAlreadyExistsException(UUID id) {
        super("Upload with id " + id + " already exists!", "id", UPLOAD__ALREADY_EXISTS);
    }

}
