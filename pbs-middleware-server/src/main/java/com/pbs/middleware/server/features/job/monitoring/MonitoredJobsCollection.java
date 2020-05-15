package com.pbs.middleware.server.features.job.monitoring;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import com.pbs.middleware.server.features.job.events.JobEvent;
import com.pbs.middleware.server.features.job.events.JobFailedEvent;
import com.pbs.middleware.server.features.job.events.JobLaunchedEvent;
import com.pbs.middleware.server.features.job.events.JobMonitoringFinishedEvent;
import com.pbs.middleware.server.features.job.events.JobRestartRequestEvent;
import com.pbs.middleware.server.features.job.events.JobUnexpectedStateEvent;
import com.pbs.middleware.server.features.job.utils.JobVisitor;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MonitoredJobsCollection extends JobVisitor {

    @Getter
    private final Map<String, Queue<MonitoredJob>> connectionQueues = new ConcurrentHashMap<>();

    @Getter
    private final Map<UUID, MonitoredJob> jobsDomainMap = new ConcurrentHashMap<>();

    @Getter
    private final Map<String, MonitoredJob> jobsIdMap = new ConcurrentHashMap<>();

    public void addQueue(String connection, Queue<MonitoredJob> queue) {
        connectionQueues.put(connection, queue);
    }

    @EventListener
    public void onApplicationEvent(final JobEvent event) throws MiddlewareException {
        this.visit(event);
    }

    @Override
    protected void apply(JobLaunchedEvent event) {
        Queue<MonitoredJob> queue = connectionQueues.get(event.getConnection());
        MonitoredJob job = new MonitoredJob(event.getDomainId(), event.getJobId(), queue);
        queue.add(job);
        jobsDomainMap.put(event.getDomainId(), job);
        jobsIdMap.put(event.getJobId(), job);
    }

    @Override
    protected void apply(JobRestartRequestEvent event) {
        stopJobMonitoring(event);
    }

    @Override
    protected void apply(JobFailedEvent event) {
        stopJobMonitoring(event);
    }

    @Override
    protected void apply(JobMonitoringFinishedEvent event) {
        stopJobMonitoring(event);
    }

    @Override
    protected void apply(JobUnexpectedStateEvent event) {
        MonitoredJob job = jobsDomainMap.get(event.getDomainId());
        job.setPause(true);
    }

    protected void stopJobMonitoring(JobEvent event) {
        MonitoredJob job = jobsDomainMap.get(event.getDomainId());
        job.getQueue().remove(job);
        jobsDomainMap.remove(event.getDomainId());
        jobsIdMap.remove(job.getJobId());
    }

}
