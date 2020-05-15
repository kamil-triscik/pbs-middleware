package com.pbs.middleware.server.features.job.utils;

import com.pbs.middleware.server.common.domain.Visitor;
import com.pbs.middleware.server.features.job.events.JobUnexpectedStateEvent;
import com.pbs.middleware.server.features.job.events.JobErrorEvent;
import com.pbs.middleware.server.features.job.events.JobEvent;
import com.pbs.middleware.server.features.job.events.JobFailedEvent;
import com.pbs.middleware.server.features.job.events.JobLaunchFailedEvent;
import com.pbs.middleware.server.features.job.events.JobLaunchedEvent;
import com.pbs.middleware.server.features.job.events.JobMonitoringFinishedEvent;
import com.pbs.middleware.server.features.job.events.JobMonitoringStartedEvent;
import com.pbs.middleware.server.features.job.events.JobMovedEvent;
import com.pbs.middleware.server.features.job.events.JobRestartRequestEvent;
import com.pbs.middleware.server.features.job.events.JobStateChangedEvent;
import com.pbs.middleware.server.features.job.events.JobStateCheckFailedEvent;
import com.pbs.middleware.server.features.job.events.JobSubmittedEvent;

public abstract class JobVisitor implements Visitor<JobEvent> {

    @Override
    public void visit(JobEvent event) {
        if (event instanceof JobSubmittedEvent) {
            apply((JobSubmittedEvent) event);
        } else if (event instanceof JobLaunchedEvent) {
            apply((JobLaunchedEvent) event);
        } else if (event instanceof JobLaunchFailedEvent) {
            apply((JobLaunchFailedEvent) event);
        } else if (event instanceof JobMonitoringStartedEvent) {
            apply((JobMonitoringStartedEvent) event);
        } else if (event instanceof JobStateCheckFailedEvent) {
            apply((JobStateCheckFailedEvent) event);
        } else if (event instanceof JobStateChangedEvent) {
            apply((JobStateChangedEvent) event);
        } else if (event instanceof JobMonitoringFinishedEvent) {
            apply((JobMonitoringFinishedEvent) event);
        } else if (event instanceof JobErrorEvent) {
            apply((JobErrorEvent) event);
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

    protected void apply(JobSubmittedEvent event) {
    }

    protected void apply(JobLaunchedEvent event) {
    }

    protected void apply(JobLaunchFailedEvent event) {
    }

    protected void apply(JobMonitoringStartedEvent event) {
    }

    protected void apply(JobStateCheckFailedEvent event) {
    }

    protected void apply(JobStateChangedEvent event) {
    }

    protected void apply(JobMonitoringFinishedEvent event) {
    }

    protected void apply(JobErrorEvent event) {
    }

    protected void apply(JobUnexpectedStateEvent event) {
    }

    protected void apply(JobFailedEvent event) {
    }

    protected void apply(JobRestartRequestEvent event) {
    }

    protected void apply(JobMovedEvent event) {
    }

}
