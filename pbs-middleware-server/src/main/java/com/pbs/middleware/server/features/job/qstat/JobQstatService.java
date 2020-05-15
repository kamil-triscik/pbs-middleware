package com.pbs.middleware.server.features.job.qstat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.server.features.job.domain.JobState;
import com.pbs.middleware.server.features.pbs.qstat.Qstat;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.pbs.middleware.server.features.pbs.qstat.Qstat.unknown;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class JobQstatService {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private final JobQstatRepository repository;

    public void save(JobQstat jobQstat) {
        repository.save(jobQstat);
    }

    public Qstat getQstat(UUID id) {
        return repository.findById(id)
                .map(JobQstat::getQstat)
                .map(qstat -> {
                    try {
                        return objectMapper.readValue(qstat, Qstat.class);
                    } catch (IOException e) {
                        log.error("QSTAT[" + id + "] deserialization error");
                    }
                    return unknown("Deserialization error");
                })
                .orElse(unknown());
    }

    public Set<JobState> getGroupState(String group) {
        return repository.findByGroup(group);
    }

    public boolean exists(UUID id) {
        return repository.findById(id).isPresent();
    }

    public void updateState(Qstat qstat, UUID id) throws JsonProcessingException {
        this.updateState(objectMapper.writeValueAsString(qstat), id);
    }

    public void updateState(String qstat, UUID id) {
        repository.updateState(qstat, id);
    }

    public void delete(UUID jobStatusId) {
        repository.deleteById(jobStatusId);
    }
}