package com.pbs.middleware.server.features.job.events;

import com.pbs.middleware.server.common.domain.MiddlewareEvent;
import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Job")
public abstract class JobEvent extends MiddlewareEvent<UUID> {

    public JobEvent(UUID domainId) {
        super(domainId);
    }

}
