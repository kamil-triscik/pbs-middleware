package com.pbs.middleware.server.features.job.exceptions;

import com.pbs.middleware.server.common.exception.NotFoundException;
import java.util.UUID;

import static com.pbs.middleware.server.features.job.exceptions.JobErrorCode.JOB_NOT_FOUND;

public final class JobNotFoundException extends NotFoundException {

    public JobNotFoundException(UUID id) {
        super("Job with id " + id + " not found!", "id", JOB_NOT_FOUND);
    }

}
