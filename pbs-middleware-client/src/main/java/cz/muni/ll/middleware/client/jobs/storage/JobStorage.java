package cz.muni.ll.middleware.client.jobs.storage;

import cz.muni.ll.middleware.client.jobs.Job;
import cz.muni.ll.middleware.client.jobs.JobState;
import java.util.List;
import java.util.Optional;

public interface JobStorage {

    void store(Job job);

    Optional<Job> find(String id);

    void setRunning(Job job);

    void setRunning(String id);

    void setFinished(Job job);

    void setFinished(String id);

    void setState(Job job, JobState state);

    void setState(String id, JobState state);

    List<Job> getAll();

    List<Job> getAllByState(JobState state);

    List<Job> getAllRunning();

    boolean deleteById(String id);

    Long deleteAll();

    Long deleteAllWithState(JobState state);

}
