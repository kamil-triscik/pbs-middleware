package com.pbs.middleware.server.common.storage.temporary;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class TemporaryFileNotFoundException extends MiddlewareException {

    public TemporaryFileNotFoundException(UUID domainId, String id) {
        super(NOT_FOUND, NOT_FOUND.getReasonPhrase().toUpperCase(), "Temporary file not found: " + domainId + "-" + id);
    }

}
