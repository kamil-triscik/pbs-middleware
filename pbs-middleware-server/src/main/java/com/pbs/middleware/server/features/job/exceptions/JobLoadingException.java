package com.pbs.middleware.server.features.job.exceptions;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import java.util.UUID;

import static com.pbs.middleware.server.features.job.exceptions.JobErrorCode.JOB_LOADING_EXCEPTION;

public final class JobLoadingException extends MiddlewareException {

    public JobLoadingException(UUID id, Throwable e) {
        super("Job with id " + id + " loading failed!", JOB_LOADING_EXCEPTION, e);
    }

}
