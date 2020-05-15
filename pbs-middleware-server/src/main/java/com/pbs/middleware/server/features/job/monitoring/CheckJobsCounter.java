package com.pbs.middleware.server.features.job.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CheckJobsCounter {

    @NonNull
    private final MonitoredJobsCollection jobsCollection;

    private Set<UUID> jobs = new CopyOnWriteArraySet<>();

    @Autowired
    public CheckJobsCounter(@NonNull MeterRegistry registry, @NonNull MonitoredJobsCollection jobsCollection) {
        this.jobsCollection = jobsCollection;
        registry.gaugeCollectionSize("jobs.monitored", Tags.empty(), jobs);
    }

    @Scheduled(fixedRateString = "${middleware.server.features.monitoring.fixed.rate:10}000")
    public void checkJobs() {
        final Set<UUID> currentlyMonitored = jobsCollection.getJobsDomainMap().keySet();
        jobs.retainAll(currentlyMonitored);
        jobs.addAll(currentlyMonitored);
    }

}
