package com.pbs.middleware.server.features.job.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.server.common.exception.MiddlewareException;
import com.pbs.middleware.server.features.job.domain.Job;
import com.pbs.middleware.server.features.job.domain.State;
import com.pbs.middleware.server.features.job.events.JobLaunchedEvent;
import com.pbs.middleware.server.features.job.factory.JobFactory;
import com.pbs.middleware.server.features.job.qstat.JobQstat;
import com.pbs.middleware.server.features.job.qstat.JobQstatService;
import com.pbs.middleware.server.features.pbs.PbsCommandFactory;
import com.pbs.middleware.server.features.pbs.qstat.Qstat;
import com.pbs.middleware.server.features.pbs.qstat.QstatCommand;
import com.pbs.middleware.server.features.ssh.shell.Result;
import com.pbs.middleware.server.features.ssh.shell.Shell;
import com.pbs.middleware.server.features.ssh.shell.ShellFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.features.pbs.qstat.Qstat.unknown;

@Log4j2
@Component
@RequiredArgsConstructor
public class QstatStartListener {

    @NonNull
    private final JobFactory jobFactory;

    @NonNull
    private final JobQstatService qstatService;

    @NonNull
    private final ShellFactory shellFactory;

    @EventListener
    public void onApplicationEvent(final JobLaunchedEvent event) throws MiddlewareException {
        Job job = jobFactory.get(event.getDomainId());
        String connection = job.getConfiguration().getConnection();
        Shell shell = shellFactory.get(connection);
        QstatCommand pbsCommand = PbsCommandFactory.qstat().build(job.getPbsJobId());

        String qstat = "UNKNOWN";

        try {
            Result response = shell.executeCommand(pbsCommand.toString());
            log.debug("QSTAT response: \n" + new JSONObject(response.getStdout()).toString(4));
            qstat = response.getStdout();
        } catch (Exception e) {
            log.error("Job state check failed!", e);
            Qstat pbsJob = unknown();
            pbsJob.setJobId(event.getJobId());
            try {
                qstat = new ObjectMapper().writeValueAsString(pbsJob);
            } catch (JsonProcessingException ex) {
                log.error("Saving init qstat failed!", ex);
            }
        }
        qstatService.save(new JobQstat(job.getId(), qstat, job.getConfiguration().getGroup(), State.UNKNOWN));

    }
}
