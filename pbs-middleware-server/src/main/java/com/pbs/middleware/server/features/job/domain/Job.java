package com.pbs.middleware.server.features.job.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pbs.middleware.server.common.domain.MiddlewareEventObject;
import com.pbs.middleware.server.common.domain.Visitable;
import com.pbs.middleware.server.common.domain.Visitor;
import com.pbs.middleware.server.features.job.events.JobUnexpectedStateEvent;
import com.pbs.middleware.server.features.job.events.JobEvent;
import com.pbs.middleware.server.features.job.events.JobFailedEvent;
import com.pbs.middleware.server.features.job.events.JobLaunchFailedEvent;
import com.pbs.middleware.server.features.job.events.JobLaunchedEvent;
import com.pbs.middleware.server.features.job.events.JobMonitoringFinishedEvent;
import com.pbs.middleware.server.features.job.events.JobMovedEvent;
import com.pbs.middleware.server.features.job.events.JobRestartRequestEvent;
import com.pbs.middleware.server.features.job.events.JobStateChangedEvent;
import com.pbs.middleware.server.features.job.events.JobSubmittedEvent;
import com.pbs.middleware.server.features.job.listeners.JobPublisher;
import com.pbs.middleware.server.features.job.utils.JobConfiguration;
import com.pbs.middleware.server.features.job.utils.JobQstatData;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Job extends MiddlewareEventObject<JobEvent> implements Visitable<JobEvent> {

    @NonNull
    @Getter
    private final UUID id;

    @NonNull
    private final JobPublisher jobPublisher;

    @Getter
    private final Queue<JobEvent> events = new ConcurrentLinkedQueue<>();

    @Getter
    @JsonIgnore
    private boolean launched = false;

    @Getter
    private String pbsJobId;

    @Getter
    private Integer exitCode = -1;

    @Getter
    private String message;

    @Getter
    private Integer restarts = 0;

    @Getter
    private UUID owner;

    @Getter
    private final JobQstatData qstatData = new JobQstatData();

    @Getter
    @JsonIgnore
    private JobConfiguration configuration;

    public State getState() {
        return qstatData.getState();
    }

    public void submit(JobConfiguration templateProps, UUID owner) {
        jobPublisher.publish(new JobSubmittedEvent(getId(), templateProps, owner));
    }

    public void fail(Integer exitCode, String message) {
        jobPublisher.publish(new JobFailedEvent(getId(), exitCode, message));
    }

    public void restart(JobConfiguration templateProps, UUID owner) {
        jobPublisher.publish(new JobSubmittedEvent(getId(), templateProps, owner));
    }

    public void apply(JobEvent event) {
        events.add(event);
        if (event instanceof JobLaunchedEvent) apply((JobLaunchedEvent) event);
        if (event instanceof JobSubmittedEvent) {
            apply((JobSubmittedEvent) event);
        } else if (event instanceof JobLaunchedEvent) {
            apply((JobLaunchedEvent) event);
        } else if (event instanceof JobLaunchFailedEvent) {
            apply((JobLaunchFailedEvent) event);
        } else if (event instanceof JobStateChangedEvent) {
            apply((JobStateChangedEvent) event);
        } else if (event instanceof JobMonitoringFinishedEvent) {
            apply((JobMonitoringFinishedEvent) event);
        } else if (event instanceof JobUnexpectedStateEvent) {
            apply((JobUnexpectedStateEvent) event);
        } else if (event instanceof JobFailedEvent) {
            apply((JobFailedEvent) event);
        } else if (event instanceof JobRestartRequestEvent) {
            apply((JobRestartRequestEvent) event);
        } else if (event instanceof JobMovedEvent) {
            apply((JobMovedEvent) event);
        }
    }

    @Override
    public void accept(Visitor<JobEvent> visitor) {
        events.forEach(visitor::visit);
    }

    private void apply(JobSubmittedEvent event) {
        this.configuration = event.getJobConfiguration();
        this.qstatData.setState(State.INITIALIZING);
        this.owner = event.getOwner();
    }

    private void apply(JobLaunchedEvent event) {
        this.launched = true;
        this.pbsJobId = event.getJobId();
        this.qstatData.setState(State.QUEUED);
    }

    private void apply(JobLaunchFailedEvent event) {
        this.qstatData.setState(State.NOT_STARTED);
    }

    private void apply(JobStateChangedEvent event) {
        event.getState().ifPresent(this.qstatData::setState);
        event.getSubstate().ifPresent(this.qstatData::setSubState);
        event.getComment().ifPresent(this.qstatData::setComment);
        event.getExecHost().ifPresent(this.qstatData::setExecHost);
        event.getExecVNode().ifPresent(this.qstatData::setExecVNode);
        event.getRunCount().ifPresent(this.qstatData::setRunCount);
    }

    private void apply(JobUnexpectedStateEvent event) {
        this.exitCode = event.getExitCode();
    }

    private void apply(JobRestartRequestEvent event) {
        this.restarts++;
        if (event.getJobConfiguration() != null) {
            this.configuration = event.getJobConfiguration();
        }
    }

    private void apply(JobFailedEvent event) {
        this.exitCode = event.getExitCode();
        this.message = event.getMessage();
        this.qstatData.setState(State.FINISHED);
    }

    private void apply(JobMovedEvent event) {
        this.pbsJobId = event.getPbsId();
    }

    private void apply(JobMonitoringFinishedEvent event) {
        this.exitCode = event.getExitCode();
    }

}
