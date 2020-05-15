package com.pbs.middleware.server.common.storage.event;

import com.pbs.middleware.server.common.exception.MiddlewareException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

final public class StorageException extends MiddlewareException {

    public StorageException(String errorMessage) {
        super(INTERNAL_SERVER_ERROR, "STORAGE_ERROR", errorMessage);
    }

    public StorageException(Exception exception) {
        super(INTERNAL_SERVER_ERROR, "STORAGE_ERROR", exception.getMessage());
    }

}
