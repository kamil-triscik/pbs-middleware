package com.pbs.middleware.server.features.contact.service;

import com.pbs.middleware.server.common.exception.NotFoundException;

public class WrongTypeException extends NotFoundException {

    private WrongTypeException(String message, String field) {
        super(message, field, null);
    }

    /**
     * Creates a new {@link WrongTypeException} with a message containing given {@code type}.
     *
     * @param type contact type
     * @return a new {@link WrongTypeException} with a message containing given {@code type}
     */
    public static WrongTypeException of(String type) {
        return new WrongTypeException(String.format("Invalid contact type: %s", type), "type");
    }

}
