package com.pbs.middleware.server.features.template.events;

import com.pbs.middleware.server.common.domain.MiddlewareEvent;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Template")
public abstract class TemplateEvent extends MiddlewareEvent<String> {

    public TemplateEvent(String domainId) {
        super(domainId);
    }

}
