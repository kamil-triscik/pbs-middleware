package com.pbs.middleware.server.features.job.listeners;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import com.pbs.middleware.server.features.job.domain.Job;
import com.pbs.middleware.server.features.job.events.JobUnexpectedStateEvent;
import com.pbs.middleware.server.features.job.events.JobNotificationRequestEvent;
import com.pbs.middleware.server.features.job.factory.JobFactory;
import com.pbs.middleware.server.features.job.handling.JobOutput;
import com.pbs.middleware.server.features.job.handling.handler.CustomScriptHandler;
import com.pbs.middleware.server.features.job.handling.handler.DefaultScriptHandler;
import com.pbs.middleware.server.features.job.handling.handler.ScriptHandler;
import com.pbs.middleware.server.features.job.qstat.JobQstatService;
import com.pbs.middleware.server.features.pbs.qstat.Qstat;
import com.pbs.middleware.server.features.script.service.ScriptService;
import com.pbs.middleware.server.features.ssh.shell.Shell;
import com.pbs.middleware.server.features.ssh.shell.ShellFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.common.utils.Optional.ofNullable;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutomatedActionRequestListener {

    @NonNull
    private final JobFactory jobFactory;

    @NonNull
    private final JobPublisher jobPublisher;

    @NonNull
    private final ScriptService scriptService;

    @NonNull
    private final JobQstatService qstatService;

    @NonNull
    private final ShellFactory shellFactory;

    @EventListener
    public void onApplicationEvent(final JobUnexpectedStateEvent event) throws MiddlewareException {
        try {
            Job job = jobFactory.get(event.getDomainId());
            Shell shell = shellFactory.get(job.getConfiguration().getConnection());
            Qstat qstat = qstatService.getQstat(event.getDomainId());

            ofNullable(job.getConfiguration().getScript())
                    .map(scriptService::findByName)
                    .filter(java.util.Optional::isPresent)
                    .map(java.util.Optional::get)
                    .map(script1 -> (ScriptHandler) new CustomScriptHandler(
                            job,
                            jobPublisher,
                            getOutput(shell, qstat),
                            qstat,
                            scriptService.getFullScript(script1)))
                    .orElse(new DefaultScriptHandler(event.getDomainId(), jobPublisher, qstat))
                    .execute();
        } catch (Exception e) {
            log.error("Job unexpected state handling failed", e);
            jobPublisher.publish(JobNotificationRequestEvent.of(event.getDomainId(),
                    "Job[" + event.getDomainId() + "] unexpected state handling failed",
                    "Exception:\n" + getStackTrace(e)));
        }

    }

    private JobOutput getOutput(Shell shell, Qstat qstat) {
        return new JobOutput(getStdOut(shell, qstat), getStderr(shell, qstat));
    }

    private String getStderr(Shell shell, Qstat qstat) {
        try {
            return shell.executeCommand("cat " + qstat.getErrorPath().split(":")[1]).getStdout();
        } catch (Exception e) {
            log.error("Loading STDERR[" + qstat.getErrorPath() + "] failed", e);
            return "";
        }
    }

    private String getStdOut(Shell shell, Qstat qstat) {
        try {
            return shell.executeCommand("cat " + qstat.getOutputPath().split(":")[1]).getStdout();
        } catch (Exception e) {
            log.error("Loading STDOUT[" + qstat.getOutputPath() + "] failed", e);
            return "";
        }
    }
}
