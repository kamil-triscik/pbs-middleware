package cz.muni.ll.middleware.client.jobs.domain.job;

import cz.muni.ll.middleware.client.jobs.JobState;
import java.util.UUID;
import lombok.Data;

@Data
public class JobInfo {

    private UUID id;

    private JobState realStatus;

    private JobState status;
}
