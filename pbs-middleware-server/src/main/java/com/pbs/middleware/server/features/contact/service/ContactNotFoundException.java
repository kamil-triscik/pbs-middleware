package com.pbs.middleware.server.features.contact.service;

import com.pbs.middleware.server.common.exception.CommonErrorCode;
import com.pbs.middleware.server.common.exception.NotFoundException;
import java.util.UUID;

/**
 * The {@code ContactNotFoundException} is thrown by {@link ContactService} when
 * a {@link com.pbs.middleware.server.features.contact.repository.Contact} is not found.
 *
 */
public class ContactNotFoundException extends NotFoundException {

    private ContactNotFoundException(String message, String field) {
        super(message, field, CommonErrorCode.NOT_FOUND);
    }

    /**
     * Creates a new {@link ContactNotFoundException} with a message containing given {@code id}.
     *
     * @param id contact id
     * @return a new {@link ContactNotFoundException} with a message containing given {@code id}
     */
    public static ContactNotFoundException of(UUID id) {
        return new ContactNotFoundException(String.format("Contact with id %s not found.", id), "id");
    }

    /**
     * Creates a new {@link ContactNotFoundException} with a message containing given {@code name}.
     *
     * @param name contact name
     * @return a new {@link ContactNotFoundException} with a message containing given {@code name}
     */
    public static ContactNotFoundException of(String name) {
        return new ContactNotFoundException(String.format("Contact with name %s not found.", name), "name");
    }


}
