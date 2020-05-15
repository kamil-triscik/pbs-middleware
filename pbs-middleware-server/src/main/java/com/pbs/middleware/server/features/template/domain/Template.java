package com.pbs.middleware.server.features.template.domain;

import com.pbs.middleware.server.common.domain.MiddlewareEventObject;
import com.pbs.middleware.server.common.domain.Visitable;
import com.pbs.middleware.server.common.domain.Visitor;
import com.pbs.middleware.common.pbs.QsubParameters;
import com.pbs.middleware.server.features.template.events.TemplateCreated;
import com.pbs.middleware.server.features.template.events.TemplateEvent;
import com.pbs.middleware.server.features.template.events.TemplateUpdated;
import com.pbs.middleware.server.features.template.listeners.TemplatePublisher;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Template extends MiddlewareEventObject<TemplateEvent> implements Visitable<TemplateEvent> {

    private Queue<TemplateEvent> events = new ConcurrentLinkedQueue<>();

    @NonNull
    @Getter
    private final String templateId;

    @NonNull
    private final TemplatePublisher publisher;

    @Getter
    private Integer version = 0;

    @Getter
    private String description;

    @Getter
    private String group;

    @Getter
    private String connection;

    @Getter
    private String script;

    private QsubParameters qsubParameters;
    @Override
    public void apply(TemplateEvent event) {
        version++;
        events.add(event);
        if (event instanceof TemplateCreated) {
            this.apply((TemplateCreated) event);
        } else if (event instanceof TemplateUpdated) {
            this.apply((TemplateUpdated) event);
        }
    }

    private void apply(TemplateCreated event) {
        this.description = event.getDescription();
        this.group = event.getGroup();
        this.connection = event.getConnection();
        this.script = event.getScript();
        this.qsubParameters = event.getQsubParameters();
    }

    private void apply(TemplateUpdated event) {
        this.description = event.getDescription();
        this.qsubParameters = event.getPbs();
        this.group = event.getGroup();
        this.connection = event.getConnection();
        this.script = event.getScript();
    }

    public void create(String description,
                       String group,
                       String connection,
                       String script,
                       QsubParameters qsubParameters) {
        publisher.publish(new TemplateCreated(getTemplateId(), description, group, connection, script, qsubParameters));
    }

    public void update(String description,
                       String group,
                       String connection,
                       String script,
                       QsubParameters qsubParameters) {
        publisher.publish(new TemplateUpdated(getTemplateId(), description, group, connection, script, qsubParameters));
    }

    @Override
    public void accept(Visitor<TemplateEvent> visitor) {
        events.forEach(visitor::visit);
    }
}
