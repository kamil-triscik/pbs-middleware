package com.pbs.middleware.api.job;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "State", description = "Enumeration of available job states.")
public enum State {

    UNKNOWN,
    NOT_STARTED,
    INITIALIZING,
    QUEUED,
    RUNNING,
    HELD,
    MOVED,
    FINISHED,
    EXITING

}
