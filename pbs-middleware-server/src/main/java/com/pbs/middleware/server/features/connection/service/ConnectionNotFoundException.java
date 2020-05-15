package com.pbs.middleware.server.features.connection.service;

import com.pbs.middleware.server.common.exception.NotFoundException;
import java.util.UUID;

import static com.pbs.middleware.server.common.exception.CommonErrorCode.NOT_FOUND;

/**
 * The {@code ConnectionNotFoundException} is thrown by {@link ConnectionService} when
 * a {@link com.pbs.middleware.server.features.connection.repository.Connection} is not found.
 *
 */
public class ConnectionNotFoundException extends NotFoundException {

    private ConnectionNotFoundException(String message, String field) {
        super(message, field, NOT_FOUND);
    }

    /**
     * Creates a new {@link ConnectionNotFoundException} with a message containing given {@code id}.
     *
     * @param id connection id
     * @return a new {@link ConnectionNotFoundException} with a message containing given {@code id}
     */
    public static ConnectionNotFoundException of(UUID id) {
        return new ConnectionNotFoundException(String.format("Connection with id %s not found.", id), "id");
    }

    /**
     * Creates a new {@link ConnectionNotFoundException} with a message containing given {@code id}.
     *
     * @param idOrName connection id or name
     * @return a new {@link ConnectionNotFoundException} with a message containing given {@code id}
     */
    public static ConnectionNotFoundException of(String idOrName) {
        return new ConnectionNotFoundException(String.format("Connection with id or name %s not found.", idOrName), "id");
    }


}
