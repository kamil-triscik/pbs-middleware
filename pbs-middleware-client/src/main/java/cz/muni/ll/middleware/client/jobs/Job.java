package cz.muni.ll.middleware.client.jobs;

import java.util.UUID;
import lombok.Data;

@Data
public final class Job {

    private String id;

    private UUID middlewareId;

    private String pbsJobId;

    private JobState state = JobState.UNKNOWN;

}
