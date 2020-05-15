package com.pbs.middleware.server.features.job.utils;

import com.pbs.middleware.common.pbs.QsubParameters;
import com.pbs.middleware.server.features.template.domain.Template;
import com.pbs.middleware.server.features.template.events.TemplateCreated;
import com.pbs.middleware.server.features.template.events.TemplateEvent;
import com.pbs.middleware.server.features.template.events.TemplateUpdated;
import com.pbs.middleware.server.features.template.utils.TemplateVisitor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PRIVATE;

//todo find better name
@Data
@NoArgsConstructor
public class JobConfiguration extends TemplateVisitor {

    public static JobConfiguration of(Template template) {
        JobConfiguration configuration = new JobConfiguration();
        configuration.setTemplateId(template.getTemplateId());
        template.accept(configuration);
        return configuration;
    }

    @Setter(PRIVATE)
    private String templateId;

    private Integer version = 0;

    private String description;

    private String group;

    private String connection;

    private String script;

    private QsubParameters qsubParameters;

    @Override
    protected void templateEvent(TemplateEvent event) {
        version++;
    }

    @Override
    protected void templateCreated(TemplateCreated event) {
        this.description = event.getDescription();
        this.group = event.getGroup();
        this.connection = event.getConnection();
        this.script = event.getScript();
        this.qsubParameters = event.getQsubParameters();
    }

    @Override
    protected void templateUpdated(TemplateUpdated event) {
        this.description = event.getDescription();
        this.qsubParameters = event.getPbs();
        this.group = event.getGroup();
        this.connection = event.getConnection();
        this.script = event.getScript();
    }
}
