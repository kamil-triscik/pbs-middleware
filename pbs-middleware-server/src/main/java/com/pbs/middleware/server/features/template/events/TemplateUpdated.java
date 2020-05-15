package com.pbs.middleware.server.features.template.events;

import com.pbs.middleware.common.pbs.QsubParameters;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "Template")
@EqualsAndHashCode(callSuper = true)
public class TemplateUpdated extends TemplateEvent {

    private String description;

    private String group;

    private String connection;

    private String script;

    private QsubParameters pbs;

    public TemplateUpdated(
            String domainId,
            String description,
            String group,
            String connection,
            String script,
            QsubParameters pbs) {
        super(domainId);
        this.description = description;
        this.group = group;
        this.connection = connection;
        this.script = script;
        this.pbs = pbs;
    }
}
