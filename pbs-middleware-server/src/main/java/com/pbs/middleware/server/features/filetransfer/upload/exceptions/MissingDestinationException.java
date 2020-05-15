package com.pbs.middleware.server.features.filetransfer.upload.exceptions;

import static com.pbs.middleware.server.features.filetransfer.upload.exceptions.UploadErrorCode.UPLOAD__MISSING_UPLOAD_DESTINATION;

public class MissingDestinationException extends WrongUploadRequestException {

    public MissingDestinationException(String destination) {
        super("Invalid upload destination: " + destination, UPLOAD__MISSING_UPLOAD_DESTINATION);
    }

}
