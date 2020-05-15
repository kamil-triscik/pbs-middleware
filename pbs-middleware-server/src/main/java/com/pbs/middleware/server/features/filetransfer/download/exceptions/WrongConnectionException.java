package com.pbs.middleware.server.features.filetransfer.download.exceptions;

import java.util.UUID;

import static java.lang.String.format;

public class WrongConnectionException extends WrongDownloadRequestException {

    public WrongConnectionException(UUID id) {
        this(id.toString());
    }

    public WrongConnectionException(String id) {
        super(format("connection with id %s does not exists!", id), "connection", DownloadErrorCode.DOWNLOAD__CONNECTION_NOT_FOUND);
    }

    public WrongConnectionException(UUID id, String message) {
        super(format("Connection[%s] exception: %s", id.toString(), message), "connection", DownloadErrorCode.DOWNLOAD__CONNECTION_NOT_FOUND);
    }

}
