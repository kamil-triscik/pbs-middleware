package com.pbs.middleware.server.features.job.service;

import com.pbs.middleware.api.job.Job;
import com.pbs.middleware.api.job.State;

public class JobMapper {

    public static Job toDto(com.pbs.middleware.server.features.job.domain.Job job) {
        return new Job(
                job.getId(),
                job.getPbsJobId(),
                State.valueOf(getState(job.getState()).toString()),
                job.getExitCode(),
                job.getMessage(),
                job.getRestarts()
        );
    }

    private static com.pbs.middleware.server.features.job.domain.State getState(com.pbs.middleware.server.features.job.domain.State state) {
        if (com.pbs.middleware.server.features.job.domain.State.NOT_STARTED.equals(state)) {
            return com.pbs.middleware.server.features.job.domain.State.NOT_STARTED;
        }
        return com.pbs.middleware.server.features.job.domain.State.FINISHED.equals(state) ? com.pbs.middleware.server.features.job.domain.State.FINISHED : com.pbs.middleware.server.features.job.domain.State.RUNNING;
    }

}
