package com.pbs.middleware.server.features.job.factory;

import com.pbs.middleware.server.common.storage.EventRepository;
import com.pbs.middleware.server.common.utils.Optional;
import com.pbs.middleware.server.features.job.domain.Job;
import com.pbs.middleware.server.features.job.events.JobEvent;
import com.pbs.middleware.server.features.job.exceptions.JobAlreadyExistsException;
import com.pbs.middleware.server.features.job.exceptions.JobNotFoundException;
import com.pbs.middleware.server.features.job.listeners.JobPublisher;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import static com.pbs.middleware.server.common.utils.Optional.empty;
import static com.pbs.middleware.server.common.utils.Optional.of;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobFactory {

    @NonNull
    private final JobPublisher publisher;

    @NonNull
    private final EventRepository<JobEvent, UUID> repository;

    /**
     * Loads a job for given id and applies all stored events on it.
     */
    public Job get(UUID id) {
        List<JobEvent> events = repository.findAllByDomainId(id);
        if (events.isEmpty()) {
            throw new JobNotFoundException(id);
        }
        Job job = new Job(id, publisher);

        events.forEach(job::apply);

        return job;
    }

    /**
     * Loads a job for given id and applies all stored events on it.
     */
    public Optional<Job> find(UUID id) {
        return find(id, Integer.MAX_VALUE);
    }

    public Optional<Job> find(UUID id, Integer version) {
        List<JobEvent> events = repository.findAllByDomainId(id);
        if (events.isEmpty()) {
            return empty();
        }
        Job job = new Job(id, publisher);

        events.stream().limit(version).forEach(job::apply);

        return of(job);
    }

    public Job create(UUID id) {
        if (!repository.findAllByDomainId(id).isEmpty()) {
            throw new JobAlreadyExistsException(id);
        }

        return new Job(id, publisher);
    }

    public void delete(UUID id) {
        repository.deleteAllByDomainId(id);
    }

}
