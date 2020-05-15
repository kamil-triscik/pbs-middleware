package com.pbs.middleware.server.features.template.utils;

import com.pbs.middleware.server.common.domain.Visitor;
import com.pbs.middleware.server.features.template.events.TemplateCreated;
import com.pbs.middleware.server.features.template.events.TemplateEvent;
import com.pbs.middleware.server.features.template.events.TemplateUpdated;

public abstract class TemplateVisitor implements Visitor<TemplateEvent> {

    @Override
    public void visit(TemplateEvent event) {
        templateEvent(event);
        if (event instanceof TemplateCreated) {
            this.templateCreated((TemplateCreated) event);
        } else if (event instanceof TemplateUpdated) {
            this.templateUpdated((TemplateUpdated) event);
        }
    }

    protected void templateEvent(TemplateEvent event) {

    }

    protected void templateCreated(TemplateCreated event) {

    }

    protected void templateUpdated(TemplateUpdated event) {

    }
}
