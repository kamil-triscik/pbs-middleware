package com.pbs.middleware.server.features.job.listeners;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import com.pbs.middleware.server.features.job.domain.State;
import com.pbs.middleware.server.features.job.events.JobUnexpectedStateEvent;
import com.pbs.middleware.server.features.job.events.JobMonitoringFinishedEvent;
import com.pbs.middleware.server.features.job.events.JobMovedEvent;
import com.pbs.middleware.server.features.job.events.JobStateChangedEvent;
import com.pbs.middleware.server.features.job.qstat.JobQstatService;
import com.pbs.middleware.server.features.pbs.qstat.Qstat;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.features.job.domain.State.HELD;
import static com.pbs.middleware.server.features.job.domain.State.UNKNOWN;

@Log4j2
@Component
@RequiredArgsConstructor
public class JobsStatusChangedListener {

    @NonNull
    private final JobPublisher jobPublisher;

    @NonNull
    private final JobQstatService qstatService;

    @EventListener
    public void onApplicationEvent(final JobStateChangedEvent event) throws MiddlewareException {
        Qstat qstat = qstatService.getQstat(event.getDomainId());

        if (isSuccess(event, qstat)) {
            jobPublisher.publish(new JobMonitoringFinishedEvent(event.getDomainId(), qstat.getExitStatus()));
            return;
        }

        if (State.MOVED.equals(event.getState().orElseGet(() -> UNKNOWN))) {
            String queue = qstat.getQueue();
            String newPbsId = qstat.getJobId() + queue.substring(queue.indexOf("@")).replaceAll(" ", "");
            log.info("Job moved. New PBS id is {}", newPbsId);
            jobPublisher.publish(new JobMovedEvent(event.getDomainId(), newPbsId));
            return;
        }

        if (isHeld(event) || isFailure(event, qstat)) {
            jobPublisher.publish(new JobUnexpectedStateEvent(
                    event.getDomainId(),
                    event.getState().get(),
                    qstat.getExitStatus()));
        }
    }

    private boolean isHeld(JobStateChangedEvent jobStateChanged) {
        return HELD.equals(jobStateChanged.getState().orElseGet(() -> UNKNOWN));
    }

    private boolean isFailure(JobStateChangedEvent jobStateChanged, Qstat qstat) {
        return State.FINISHED.equals(jobStateChanged.getState().orElseGet(() -> UNKNOWN))
                && qstat.getExitStatus() != 0;
    }

    private boolean isSuccess(JobStateChangedEvent jobStateChanged, Qstat qstat) {
        return State.FINISHED.equals(jobStateChanged.getState().orElseGet(() -> UNKNOWN))
                && qstat.getExitStatus() == 0;
    }
}
