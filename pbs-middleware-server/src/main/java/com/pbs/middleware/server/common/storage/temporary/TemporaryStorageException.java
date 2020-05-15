package com.pbs.middleware.server.common.storage.temporary;

public class TemporaryStorageException extends RuntimeException {

    public TemporaryStorageException(String message) {
        super(message);
    }

    public TemporaryStorageException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
