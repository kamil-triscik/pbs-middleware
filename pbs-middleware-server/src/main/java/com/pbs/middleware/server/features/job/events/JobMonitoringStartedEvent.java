package com.pbs.middleware.server.features.job.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when job monitoring started.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Job")
public class JobMonitoringStartedEvent extends JobEvent {

    public JobMonitoringStartedEvent(UUID domainId) {
        super(domainId);
    }
}
