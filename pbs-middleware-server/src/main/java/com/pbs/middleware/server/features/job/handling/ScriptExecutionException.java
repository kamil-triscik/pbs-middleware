package com.pbs.middleware.server.features.job.handling;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import java.util.UUID;

import static com.pbs.middleware.server.features.job.exceptions.JobErrorCode.HANDLING_SCRIPT_EXECUTION_FAILED;

public final class ScriptExecutionException extends MiddlewareException {

    public ScriptExecutionException(UUID id, Throwable e) {
        super("Execution handling script with id [" + id + "] failed!", HANDLING_SCRIPT_EXECUTION_FAILED, e);
    }

}
