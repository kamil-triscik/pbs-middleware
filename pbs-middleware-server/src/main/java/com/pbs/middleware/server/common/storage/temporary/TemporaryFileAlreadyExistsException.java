package com.pbs.middleware.server.common.storage.temporary;

public class TemporaryFileAlreadyExistsException extends Exception {

    public TemporaryFileAlreadyExistsException(String message) {
        super(message);
    }

    public TemporaryFileAlreadyExistsException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
