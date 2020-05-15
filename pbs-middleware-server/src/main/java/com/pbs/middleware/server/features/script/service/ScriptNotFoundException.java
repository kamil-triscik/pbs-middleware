package com.pbs.middleware.server.features.script.service;

import com.pbs.middleware.server.common.exception.CommonErrorCode;
import com.pbs.middleware.server.common.exception.NotFoundException;
import java.util.UUID;

/**
 * The {@code ScriptNotFoundException} is thrown by {@link ScriptService} when
 * a {@link com.pbs.middleware.server.features.script.repository.Script} is not found.
 *
 */
public class ScriptNotFoundException extends NotFoundException {

    private ScriptNotFoundException(String message, String field) {
        super(message, field, CommonErrorCode.NOT_FOUND);
    }

    /**
     * Creates a new {@link ScriptNotFoundException} with a message containing given {@code id}.
     *
     * @param id script id
     * @return a new {@link ScriptNotFoundException} with a message containing given {@code id}
     */
    public static ScriptNotFoundException of(UUID id) {
        return new ScriptNotFoundException(String.format("Script with id %s not found.", id), "id");
    }

    /**
     * Creates a new {@link ScriptNotFoundException} with a message containing given {@code name}.
     *
     * @param name script name
     * @return a new {@link ScriptNotFoundException} with a message containing given {@code name}
     */
    public static ScriptNotFoundException of(String name) {
        return new ScriptNotFoundException(String.format("Script with name %s not found.", name), "name");
    }


}
