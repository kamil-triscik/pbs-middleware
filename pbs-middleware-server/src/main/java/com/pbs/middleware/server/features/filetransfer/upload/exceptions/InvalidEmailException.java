package com.pbs.middleware.server.features.filetransfer.upload.exceptions;

import static com.pbs.middleware.server.features.filetransfer.upload.exceptions.UploadErrorCode.UPLOAD__INVALID_EMAIL;

public class InvalidEmailException extends WrongUploadRequestException {

    public InvalidEmailException(String email) {
        super("Invalid email: " + email, UPLOAD__INVALID_EMAIL);
    }

}
