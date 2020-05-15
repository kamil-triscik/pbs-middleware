package com.pbs.middleware.server.features.job.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Job")
public class JobMonitoringFinishedEvent extends JobEvent {

    private Integer exitCode = -1;

    public JobMonitoringFinishedEvent(UUID domainId, Integer exitCode) {
        super(domainId);
        this.exitCode = exitCode;
    }
}
