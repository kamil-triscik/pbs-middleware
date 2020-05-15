package com.pbs.middleware.server.features.job.domain;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.pbs.middleware.server.features.job.domain.State.FINISHED;
import static com.pbs.middleware.server.features.job.domain.State.RUNNING;

@NoArgsConstructor
public class JobState {

    @Getter
    @Setter
    private UUID id;

    @Getter
    private State status;

    public JobState(UUID id, State status) {
        this.id = id;
        this.status = status;
    }

    public State getStatus() {
        return FINISHED.equals(status) ? FINISHED : RUNNING;
    }
}
