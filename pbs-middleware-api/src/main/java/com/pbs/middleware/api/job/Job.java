package com.pbs.middleware.api.job;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@ApiModel(value = "Job", description = "Job DTO for readonly requests.")
@RequiredArgsConstructor
public final class Job {

    @ApiModelProperty(name = "id", value = "Job ID")
    private final UUID id;

    @ApiModelProperty(name = "pbsJobId", value = "PBS job ID")
    private final String pbsJobId;

    @ApiModelProperty(name = "state", value = "Job current state", allowableValues = "UNKNOWN, NOT_STARTED, INITIALIZING, QUEUED, RUNNING, HELD, MOVED, FINISHED, EXITING")
    private final State state;

    @ApiModelProperty(name = "exitCode", value = "Job exit code. (-1 for job still running)")
    private final Integer exitCode;

    @ApiModelProperty(name = "message", value = "Job error message. In case JOB finish with error.")
    private final String message;

    @ApiModelProperty(name = "restarts", value = "Number how many times was job restarted.")
    private final Integer restarts;

}
