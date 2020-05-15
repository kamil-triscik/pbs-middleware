package com.pbs.middleware.server.features.job.monitoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pbs.middleware.server.common.utils.Optional;
import com.pbs.middleware.server.features.job.domain.Job;
import com.pbs.middleware.server.features.job.events.JobStateChangedEvent;
import com.pbs.middleware.server.features.job.events.JobStateCheckFailedEvent;
import com.pbs.middleware.server.features.job.factory.JobFactory;
import com.pbs.middleware.server.features.job.listeners.JobPublisher;
import com.pbs.middleware.server.features.job.qstat.JobQstatService;
import com.pbs.middleware.server.features.pbs.qstat.Qstat;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.common.utils.Optional.empty;
import static com.pbs.middleware.server.common.utils.Optional.of;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ObjectUtils.notEqual;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobsStatusTester {

    @NonNull
    private final JobFactory jobFactory;

    @NonNull
    private final JobPublisher jobPublisher;

    @NonNull
    private final JobQstatService qstatService;

    public void checkJobState(MonitoredJob monitoredJob, Qstat qstat) {
        try {
            MDC.put("objectId", "job-" + monitoredJob.getJobId());
            log.info("Checking job[" + monitoredJob.getDomainId() + "] state");
            jobFactory.find(monitoredJob.getDomainId()).also(job -> this.checkJobState(job, qstat));
        } finally {
            MDC.remove("objectId");
        }
    }

    private void checkJobState(Job job, Qstat qstat) {
        try {
            Qstat storedQstat = qstatService.getQstat(job.getId());
            updateStatus(qstat, storedQstat);

            jobChanged(job.getId(), qstat, storedQstat).ifPresentOrElse(
                    jobStateChanged -> {
                        try {
                            qstatService.updateState(qstat, job.getId());
                            log.info("Job(" + job.getId() + ") state changed: " + job.getState() + " -> " + qstat.getJobState());
                            jobPublisher.publish(jobStateChanged);
                        } catch (JsonProcessingException e) {
                            log.error("Qstat update failed", e);
                            throw new RuntimeException(e);
                        }
                    }, () -> {
                        try {
                            qstatService.updateState(storedQstat, job.getId());
                        } catch (JsonProcessingException e) {
                            log.error("Qstat update failed", e);
                            throw new RuntimeException(e);
                        }
                    });

        } catch (Exception e) {
            log.error("Job state check failed!", e);
            jobPublisher.publish(new JobStateCheckFailedEvent(job.getId(), e.getLocalizedMessage()));
        }
        log.info("Job state checked");
    }

    private void updateStatus(Qstat newStatus, Qstat origStatus) {
        newStatus.setWalltimes(origStatus.getWalltimes());
        newStatus.setCput(origStatus.getCput());

        ofNullable(newStatus.getResourcesUsed().get("walltime"))
                .map(String.class::cast)
                .ifPresent(walltime -> newStatus.getWalltimes().add(walltime));

        ofNullable(newStatus.getResourcesUsed().get("cput"))
                .map(String.class::cast)
                .ifPresent(cput -> newStatus.getCput().add(cput));
    }

    private Optional<JobStateChangedEvent> jobChanged(UUID jobId, Qstat newQstat, Qstat stored) {
        JobStateChangedEvent jobStateChanged = new JobStateChangedEvent(jobId);

        if (notEqual(stored.getJobState(), newQstat.getJobState())) {
            jobStateChanged.setState(newQstat.getJobState());
        }

        if (notEqual(stored.getSubstate(), newQstat.getSubstate())) {
            jobStateChanged.setSubstate(newQstat.getSubstate());
        }

        if (notEqual(stored.getComment(), newQstat.getComment())) {
            jobStateChanged.setComment(newQstat.getComment());
        }

        if (notEqual(stored.getExecHost(), newQstat.getExecHost())) {
            jobStateChanged.setExecHost(newQstat.getExecHost());
        }

        if (notEqual(stored.getExecVnode(), newQstat.getExecHost())) {
            jobStateChanged.setExecVNode(newQstat.getExecVnode());
        }

        if (notEqual(stored.getRunCount(), newQstat.getRunCount())) {
            jobStateChanged.setRunCount(newQstat.getRunCount());
        }

        return jobStateChanged.getChanges().isEmpty() ? empty() : of(jobStateChanged);
    }
}

