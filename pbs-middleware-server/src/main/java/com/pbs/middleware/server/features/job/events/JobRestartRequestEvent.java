package com.pbs.middleware.server.features.job.events;

import com.pbs.middleware.server.features.job.utils.JobConfiguration;
import java.time.Duration;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when client ask for job restart.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Job")
public class JobRestartRequestEvent extends JobEvent implements LaunchEvent {

    private JobConfiguration jobConfiguration;

    private Duration duration;

    private UUID owner;

    public JobRestartRequestEvent(UUID domainId, JobConfiguration jobConfiguration, Duration duration, UUID owner) {
        super(domainId);
        this.jobConfiguration = jobConfiguration;
        this.duration = duration;
        this.owner = owner;
    }
}
