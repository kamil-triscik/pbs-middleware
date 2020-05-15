package com.pbs.middleware.server.features.job.listeners;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import com.pbs.middleware.server.features.job.domain.Job;
import com.pbs.middleware.server.features.job.events.JobErrorEvent;
import com.pbs.middleware.server.features.job.events.JobEvent;
import com.pbs.middleware.server.features.job.events.JobLaunchFailedEvent;
import com.pbs.middleware.server.features.job.events.JobLaunchedEvent;
import com.pbs.middleware.server.features.job.events.LaunchEvent;
import com.pbs.middleware.server.features.job.events.JobNotificationRequestEvent;
import com.pbs.middleware.server.features.job.events.JobRestartRequestEvent;
import com.pbs.middleware.server.features.job.events.JobSubmittedEvent;
import com.pbs.middleware.server.features.job.factory.JobFactory;
import com.pbs.middleware.server.features.pbs.PbsCommandFactory;
import com.pbs.middleware.server.features.pbs.qsub.QsubCommand;
import com.pbs.middleware.server.features.ssh.shell.Result;
import com.pbs.middleware.server.features.ssh.shell.Shell;
import com.pbs.middleware.server.features.ssh.shell.ShellFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobSubmittedListener {

    @NonNull
    private final JobFactory jobFactory;

    @NonNull
    private final JobPublisher jobPublisher;

    @NonNull
    private final ShellFactory shellFactory;

    /**
     * Method will prepare shell object and command which will start pbs job.
     * After successful job start JobCreatedEvent is dispatched otherwise JobCreationFailed.
     *
     * @param event event we are going to process
     * @throws MiddlewareException exception not thrown.
     */
    @EventListener
    public void onApplicationEvent(final JobSubmittedEvent event) throws MiddlewareException {
        launchJob(event);
    }

    @EventListener
    public void onApplicationEvent(final JobRestartRequestEvent event) throws MiddlewareException {
        launchJob(event);
    }

    private void launchJob(LaunchEvent jobSubmitted) {
        new Thread(() -> {
            MDC.put("objectId", "job-" + ((JobEvent) jobSubmitted).getDomainId());
            launchNewJob(jobSubmitted);
            MDC.remove("objectId");
        }).start();

    }

    private void launchNewJob(LaunchEvent jobLauncher) {
        QsubCommand qsubCommand;
        String jobId;
        final String action = jobLauncher instanceof JobSubmittedEvent ? "launching" : "restart";
        try {
            log.info("Job {}!", action);
            Job job = jobFactory.get(jobLauncher.getDomainId());
            Shell shell = getShell(job);
            qsubCommand = getPbsCommand(job);
            log.info("QSUB command: " + qsubCommand.toString());
            Result result = shell.executeCommand(qsubCommand.toString());
            jobId = result.getStdout().replaceAll("\n", "");
            log.info("QSUB response: " + jobId);
            if (isBlank(jobId)) {
                throw new IllegalStateException("Returned (from qsub) job id is blank! Stderr: " + result.getStderr());
            }
            try {
                jobPublisher.publish(new JobLaunchedEvent(
                        jobLauncher.getDomainId(),
                        qsubCommand,
                        jobId,
                        job.getConfiguration().getConnection(),
                        job.getConfiguration().getGroup(),
                        jobLauncher.getOwner()));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                jobPublisher.publish(new JobErrorEvent(jobLauncher.getDomainId(), "JobLaunched event probably not notified"));
                jobPublisher.publish(JobNotificationRequestEvent.monitoringNotStarted(jobLauncher.getDomainId(), jobLauncher.getOwner(), jobId, e));
            }
        } catch (Exception e) {
            String errorMessage = e instanceof MiddlewareException ? ((MiddlewareException) e).getErrorMessage() : e.getMessage();
            errorMessage = errorMessage == null ? e.getClass().toString() : errorMessage;
            log.error("Job " + action + " failed - " + errorMessage, e);
            jobPublisher.publish(new JobLaunchFailedEvent(jobLauncher.getDomainId(), errorMessage));
        }
    }

    /**
     * Method will fetch UUID of connection which should be used for communication
     * and use {@link com.pbs.middleware.server.features.ssh.shell.ShellFactory} to
     * prepare shell for launch PBS commands.
     *
     * @param job event which contain info, which connection should be used for communication.
     * @return initialized shell object.
     */
    private Shell getShell(Job job) {
        String connection = job.getConfiguration().getConnection();
        return shellFactory.get(connection);
    }

    /**
     * Method use {@link com.pbs.middleware.server.features.pbs.PbsCommandFactory} to
     * build pbs command to start PBS job.
     *
     * @param job event which contain info, how command should looks like.
     * @return build PBS command object.
     */
    private QsubCommand getPbsCommand(Job job) {
        return PbsCommandFactory.qsub().build(job.getConfiguration());
    }
}
