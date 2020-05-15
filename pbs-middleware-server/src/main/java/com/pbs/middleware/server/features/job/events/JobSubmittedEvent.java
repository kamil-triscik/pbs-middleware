package com.pbs.middleware.server.features.job.events;

import com.pbs.middleware.server.features.job.utils.JobConfiguration;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when client submit new job.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Job")
public class JobSubmittedEvent extends JobEvent implements LaunchEvent {

    private JobConfiguration jobConfiguration;

    private UUID owner;

    public JobSubmittedEvent(UUID domainId, JobConfiguration jobConfiguration, UUID owner) {
        super(domainId);
        this.jobConfiguration = jobConfiguration;
        this.owner = owner;
    }
}
