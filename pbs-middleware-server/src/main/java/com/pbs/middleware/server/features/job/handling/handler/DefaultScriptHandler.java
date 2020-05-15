package com.pbs.middleware.server.features.job.handling.handler;

import com.pbs.middleware.server.features.job.events.JobFailedEvent;
import com.pbs.middleware.server.features.job.events.JobNotificationRequestEvent;
import com.pbs.middleware.server.features.job.listeners.JobPublisher;
import com.pbs.middleware.server.features.pbs.qstat.Qstat;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import static com.pbs.middleware.server.features.job.domain.State.FINISHED;
import static com.pbs.middleware.server.features.job.domain.State.HELD;

@Log
@RequiredArgsConstructor
public class DefaultScriptHandler implements ScriptHandler {

    private final UUID jobId;

    @NonNull
    private final JobPublisher jobPublisher;

    @NonNull
    private final Qstat jobQstat;

    @Override
    public void execute() {
        if (HELD.equals(jobQstat.getJobState())) {
            jobPublisher.publish(JobNotificationRequestEvent.of(
                    jobId,
                    "Job " + jobId + " unexpected state",
                    "QSUB:\n\n\n" + jobQstat.toString()));
        } else if (FINISHED.equals(jobQstat.getJobState()) && jobQstat.getExitStatus() != 0) {
            log.warning("Exiting job with exit code: " + jobQstat.getExitStatus());
            jobPublisher.publish(new JobFailedEvent(jobId, jobQstat.getExitStatus(), null));
        } // todo frozen
    }
}
