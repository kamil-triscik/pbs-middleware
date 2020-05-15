package com.pbs.middleware.server.features.filetransfer.upload.exceptions;

import java.util.UUID;

import static com.pbs.middleware.server.features.filetransfer.upload.exceptions.UploadErrorCode.UPLOAD__UNEXPECTED_FILENAME;
import static java.lang.String.format;

public class UnexpectedFileException extends WrongUploadRequestException {

    public UnexpectedFileException(UUID id, String filename) {
        super(format("File with name %s wasn't expected for upload %s!", filename, id), UPLOAD__UNEXPECTED_FILENAME);
    }

}
