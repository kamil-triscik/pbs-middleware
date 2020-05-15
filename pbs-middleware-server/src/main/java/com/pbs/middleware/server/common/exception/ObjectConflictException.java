package com.pbs.middleware.server.common.exception;

import static java.lang.String.format;

public class ObjectConflictException extends RuntimeException {

    public ObjectConflictException(String message) {
        super(message);
    }

    public static <E> ObjectConflictException of(Class<E> type, String value) {
        return new ObjectConflictException(format("%s with value '%s' already exists.", type.getSimpleName(), value));
    }
}
