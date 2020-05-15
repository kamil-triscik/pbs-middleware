package com.pbs.middleware.server.features.job.handling;

import com.pbs.middleware.server.features.job.events.JobFailedEvent;
import com.pbs.middleware.server.features.job.events.JobNotificationRequestEvent;
import com.pbs.middleware.server.features.job.events.JobRestartRequestEvent;
import com.pbs.middleware.server.features.job.handling.containers.JobConfigurationContainer;
import com.pbs.middleware.server.features.job.listeners.JobPublisher;
import com.pbs.middleware.server.features.job.utils.JobConfiguration;
import java.time.Duration;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JobServiceProvider {

    @NonNull
    private final UUID id;

    @NonNull
    private final UUID owner;

    @NonNull
    private final JobPublisher jobPublisher;


    public void fail() {
        fail(null, null);
    }

    public void fail(Integer exitCode) {
        fail(exitCode, null);
    }

    public void fail(Integer exitCode, String message) {
        jobPublisher.publish(new JobFailedEvent(id, exitCode, message));
    }

    public void notify(String email, String subject, String text) {
        jobPublisher.publish(JobNotificationRequestEvent.of(id, email, subject, text));
    }

    public void notify(String subject, String text) {
        jobPublisher.publish(JobNotificationRequestEvent.of(id, subject, text));
    }

    public void restart() {
        restart(null, null);
    }

    public void restart(String delay) {
        restart(null, delay);

    }

    public void restart(JobConfigurationContainer configuration) {
        restart(configuration, null);
    }

    public void restart(JobConfigurationContainer configuration, String delay) {
        final Duration duration = delay == null ? null : Duration.parse(delay);
        final JobConfiguration props = configuration != null ? configuration.getTemplateProperties() : null;
        jobPublisher.publish(new JobRestartRequestEvent(id, props, duration, owner));
    }

}
