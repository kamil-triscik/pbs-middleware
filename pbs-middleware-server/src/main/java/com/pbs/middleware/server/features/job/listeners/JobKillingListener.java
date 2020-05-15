package com.pbs.middleware.server.features.job.listeners;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import com.pbs.middleware.server.features.job.domain.Job;
import com.pbs.middleware.server.features.job.domain.State;
import com.pbs.middleware.server.features.job.events.JobEvent;
import com.pbs.middleware.server.features.job.events.JobFailedEvent;
import com.pbs.middleware.server.features.job.events.JobRestartRequestEvent;
import com.pbs.middleware.server.features.job.factory.JobFactory;
import com.pbs.middleware.server.features.job.qstat.JobQstatService;
import com.pbs.middleware.server.features.pbs.qdel.QdelCommand;
import com.pbs.middleware.server.features.ssh.shell.Result;
import com.pbs.middleware.server.features.ssh.shell.ShellException;
import com.pbs.middleware.server.features.ssh.shell.ShellFactory;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * {@link org.springframework.context.event.EventListener} to kill running jobs.
 * <p>
 * Listener is listening for {@link com.pbs.middleware.server.features.job.events.JobRestartRequestEvent}
 * and {@link com.pbs.middleware.server.features.job.events.JobFailedEvent} ff
 *
 *
 *
 *
 * @since 1.0
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class JobKillingListener {

    @NonNull
    private final JobFactory jobFactory;

    @NonNull
    private final JobQstatService qstatService;

    @NonNull
    private final ShellFactory shellFactory;

    @EventListener
    public void onApplicationEvent(final JobRestartRequestEvent event) throws MiddlewareException {
        killJob(event);
    }

    @EventListener
    public void onApplicationEvent(final JobFailedEvent event) throws MiddlewareException {
        killJob(event);
    }

    private void killJob(final JobEvent event) {
        Job job = jobFactory.get(event.getDomainId());
        killJob(job.getId(), job.getPbsJobId(), job.getConfiguration().getConnection());
    }

    private void killJob(UUID jobId, String pbsJobId, String connection) {
        if (!State.FINISHED.equals(qstatService.getQstat(jobId).getJobState())) {
            try {
                final String qdelCommand = new QdelCommand(pbsJobId).toString();
                log.info("Killing {} job with command {}", pbsJobId, qdelCommand);
                Result res = shellFactory.get(connection).executeCommand(qdelCommand);
                log.info("Job killed with result:\n" + res.toString());
            } catch (ShellException e) {
                log.error("Killing job in state HELD failed", e);
            }
        }
    }
}
