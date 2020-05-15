package com.pbs.middleware.server.features.filetransfer.upload.exceptions;

import java.util.UUID;

import static com.pbs.middleware.server.features.filetransfer.upload.exceptions.UploadErrorCode.UPLOAD__CONNECTION_NOT_FOUND;
import static java.lang.String.format;

public class WrongConnectionException extends WrongUploadRequestException {

    public WrongConnectionException(UUID id) {
        this(id.toString());
    }

    public WrongConnectionException(String id) {
        super(format("connection with id %s does not exists!", id), UPLOAD__CONNECTION_NOT_FOUND);
    }

    public WrongConnectionException(UUID id, String message) {
        super(id.toString(), message, UPLOAD__CONNECTION_NOT_FOUND);
    }

    public WrongConnectionException(String id, String message) {
        super(format("Connection[%s] exception: %s", id, message), UPLOAD__CONNECTION_NOT_FOUND);
    }

}
