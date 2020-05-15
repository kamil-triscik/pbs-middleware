package com.pbs.middleware.server.features.job.service;

import com.pbs.middleware.api.job.Job;
import com.pbs.middleware.api.job.JobRestartRequest;
import com.pbs.middleware.api.job.KillJobRequest;
import com.pbs.middleware.api.job.JobSubmit;
import com.pbs.middleware.server.features.job.domain.JobState;
import com.pbs.middleware.server.features.job.events.JobEvent;
import com.pbs.middleware.server.features.job.exceptions.JobNotFoundException;
import com.pbs.middleware.server.features.job.factory.JobFactory;
import com.pbs.middleware.server.features.job.qstat.JobQstatService;
import com.pbs.middleware.server.features.job.utils.JobConfiguration;
import com.pbs.middleware.server.features.job.utils.merge.JobConfigurationMerger;
import com.pbs.middleware.server.features.ownership.domain.DomainType;
import com.pbs.middleware.server.features.ownership.service.OwnershipService;
import com.pbs.middleware.server.features.pbs.qstat.Qstat;
import com.pbs.middleware.server.features.pbs.service.Mapper;
import com.pbs.middleware.server.features.template.domain.Template;
import com.pbs.middleware.server.features.template.service.TemplateService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class JobService {

    @NonNull
    private final JobFactory factory;

    @NonNull
    private final TemplateService templateService;

    @NonNull
    private final JobQstatService qstatService;

    @NonNull
    private final OwnershipService ownerService;

    public UUID submitJob(@NotNull UUID id, @NotNull @Valid JobSubmit body) {
        com.pbs.middleware.server.features.job.domain.Job job = factory.create(id);
        MDC.put("objectId", "job-" + id.toString());
        job.submit(getJobConfiguration(body.getTemplateId(), Mapper.mapToJobConfiguration(body)), ownerService.getLoggedUserUuid());
        MDC.remove("objectId");
        return id;
    }

    public Set<UUID> getAllJobs() {
        return ownerService.getAll(DomainType.JOB);
    }

    public Job get(UUID id) {
        return JobMapper.toDto(factory.get(id));
    }

    public Qstat getQstat(UUID id) {
        factory.get(id);
        return qstatService.getQstat(id);
    }

    public List<JobEvent> getEvents(UUID id) {
        factory.get(id);
        return new ArrayList<>(factory.get(id).getEvents());
    }

    public Set<JobState> getGroupState(String group) {
        return qstatService.getGroupState(group);
    }

    public void delete(UUID domainId) {
        factory.delete(domainId);
        ownerService.delete(domainId);
    }

    public void failed(UUID id, KillJobRequest jobFailed) {
        factory.find(id)
                .also(job -> MDC.put("objectId", "job-" + id.toString()))
                .also(job -> job.fail(jobFailed.getExitCode(), jobFailed.getReason()))
                .also(job -> MDC.remove("objectId"))
                .orElseThrow(() -> new JobNotFoundException(id));
    }

    public void restart(@NotNull UUID id, @NotNull @Valid JobRestartRequest body) {
        factory.find(id)
                .also(job -> MDC.put("objectId", "job-" + id.toString()))
                .also(job -> job.restart(getJobConfiguration(body.getTemplateId(), Mapper.mapToJobConfiguration(body)), ownerService.getLoggedUserUuid()))
                .also(job -> MDC.remove("objectId"))
                .orElseThrow(() -> new JobNotFoundException(id));
    }

    private JobConfiguration getJobConfiguration(@NonNull String templateId, JobConfiguration newJobConfiguration) {
        if (templateId == null) {
            return newJobConfiguration;
        }

        Template template = templateService.getTemplate(templateId);
        JobConfiguration orig = JobConfiguration.of(template);
        return JobConfigurationMerger.merge(orig, newJobConfiguration);
    }

}
