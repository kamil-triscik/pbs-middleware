package com.pbs.middleware.server.features.users.service;

import static com.pbs.middleware.server.common.exception.CommonErrorCode.NOT_FOUND;

import com.pbs.middleware.server.common.exception.NotFoundException;
import java.util.UUID;

/**
 * The {@code UserNotFoundException} is thrown by {@link UserService} when
 * a {@link com.pbs.middleware.server.features.users.repository.User} is not found.
 *
 */
public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String message, String field) {
        super(message, field, NOT_FOUND);
    }

    /**
     * Creates a new {@link UserNotFoundException} with a message containing given {@code id}.
     *
     * @param id user id
     * @return a new {@link UserNotFoundException} with a message containing given {@code id}
     */
    public static UserNotFoundException of(UUID id) {
        return new UserNotFoundException(String.format("User with id %s not found.", id), "id");
    }

    /**
     * Creates a new {@link UserNotFoundException} with message containing given {@code username}.
     * @param username username
     * @return a new {@link UserNotFoundException} with message containing given {@code username}
     */
    public static UserNotFoundException of(String username) {
        return new UserNotFoundException(String.format("User with username %s not found.", username), "username");
    }

}
