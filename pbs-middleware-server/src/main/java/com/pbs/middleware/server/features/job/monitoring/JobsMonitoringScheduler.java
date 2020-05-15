package com.pbs.middleware.server.features.job.monitoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.server.common.storage.EventRepository;
import com.pbs.middleware.server.features.connection.repository.ConnectionRepository;
import com.pbs.middleware.server.features.connection.repository.Connection;
import com.pbs.middleware.server.features.job.JobsConfiguration;
import com.pbs.middleware.server.features.job.events.JobEvent;
import com.pbs.middleware.server.features.pbs.qstat.QstatCommand;
import com.pbs.middleware.server.features.pbs.qstat.QstatCommandBuilder;
import com.pbs.middleware.server.features.pbs.qstat.QstatResponse;
import com.pbs.middleware.server.features.ssh.shell.Result;
import com.pbs.middleware.server.features.ssh.shell.Shell;
import com.pbs.middleware.server.features.ssh.shell.ShellException;
import com.pbs.middleware.server.features.ssh.shell.ShellFactory;
import java.util.ArrayList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Component
@RequiredArgsConstructor
//@EnableAsync - learn
public class JobsMonitoringScheduler {

    @NonNull
    private final JobsStatusTester jobsStatusTester;

    @NonNull
    private final ShellFactory shellFactory;

    @NonNull
    private final EventRepository<JobEvent, UUID> jobRepository;

    @NonNull
    private final ConnectionRepository connectionRepository;

    @NonNull
    private final MonitoredJobsCollection jobs;

    @PostConstruct
    private void init() {
        connectionRepository.findAll().stream().map(Connection::getName)
                .forEach(connection -> jobs.addQueue(connection, new ConcurrentLinkedQueue<>()));

        jobRepository.findAll().forEach(jobs::visit);
    }

    @Scheduled(fixedDelayString = JobsConfiguration.JOB_MONITORING_DELAY)
    public void checkJobs() {
        processConnections();
    }

    private void processConnections() {
        jobs.getConnectionQueues().forEach((connectionName, queue) ->
            connectionRepository.findByName(connectionName).ifPresentOrElse(connection ->
                    processConnection(connection, queue), () -> log.warn("No connection"))
        );
    }

    private void processConnection(Connection connection, Queue<MonitoredJob> queue) {
        Shell shell = shellFactory.get(connection);
        ListUtils.partition(new ArrayList<>(queue), 20).forEach(jobs -> {
            QstatCommand qstatCommand = new QstatCommandBuilder().build(
                    jobs.stream()
                            .filter(monitoredJob -> !monitoredJob.getPause())
                            .map(MonitoredJob::getJobId)
                            .collect(toSet()));
            try {
                Result res = shell.executeCommand(qstatCommand.toString());
                new ObjectMapper().readValue(res.getStdout(), QstatResponse.class).getQstats().forEach(
                        (jobId, qstat) -> jobsStatusTester.checkJobState(this.jobs.getJobsIdMap().get(jobId), qstat));
            } catch (ShellException e) {
                log.error("Qstat execution failed", e);
            } catch (JsonProcessingException e) {
                log.error("Qstat deserialization failed", e);
            }
        });
    }

}
