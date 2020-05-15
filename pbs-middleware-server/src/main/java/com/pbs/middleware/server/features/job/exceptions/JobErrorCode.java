package com.pbs.middleware.server.features.job.exceptions;

import com.pbs.middleware.server.common.exception.ErrorCode;

public enum JobErrorCode implements ErrorCode {

    JOB_ALREADY_EXISTS("Job with provided id already exist."),
    JOB_LOADING_EXCEPTION("Job loading from DB problem."),
    JOB_NOT_FOUND("Job with provided id does not exists."),
    HANDLING_SCRIPT_EXECUTION_FAILED("Handling script execution failed");

    public final String description;


    JobErrorCode(final String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
