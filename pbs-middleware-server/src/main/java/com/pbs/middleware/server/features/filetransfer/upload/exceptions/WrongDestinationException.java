package com.pbs.middleware.server.features.filetransfer.upload.exceptions;

import static com.pbs.middleware.server.features.filetransfer.upload.exceptions.UploadErrorCode.UPLOAD__INVALID_UPLOAD_DESTINATION;

public class WrongDestinationException extends WrongUploadRequestException {

    public WrongDestinationException(String message) {
        super(message, UPLOAD__INVALID_UPLOAD_DESTINATION);
    }

}
