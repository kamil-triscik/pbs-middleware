package com.pbs.middleware.server.features.filetransfer.download.exceptions;

public class InvalidEmailException extends WrongDownloadRequestException {

    public InvalidEmailException(String email) {
        super("Invalid email: " + email, "email", DownloadErrorCode.DOWNLOAD__INVALID_EMAIL);
    }

}
