package com.pbs.middleware.server.features.job.exceptions;

import com.pbs.middleware.server.common.exception.AlreadyExistsException;
import java.util.UUID;

public final class JobAlreadyExistsException extends AlreadyExistsException {

    public JobAlreadyExistsException(UUID id) {
        super("Job with id " + id + " already exists!", "id", JobErrorCode.JOB_ALREADY_EXISTS);
    }

}
