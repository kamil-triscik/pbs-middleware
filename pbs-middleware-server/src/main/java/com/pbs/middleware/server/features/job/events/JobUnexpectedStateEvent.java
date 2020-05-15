package com.pbs.middleware.server.features.job.events;

import com.pbs.middleware.server.features.job.domain.State;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used for process action in case of unexpected job state or in case of failed job.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Job")
public class JobUnexpectedStateEvent extends JobEvent {

    private State state;

    private Integer exitCode;

    public JobUnexpectedStateEvent(UUID domainId, State state, Integer exitCode) {
        super(domainId);
        this.state = state;
        this.exitCode = exitCode;
    }
}
