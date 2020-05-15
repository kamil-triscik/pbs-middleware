package com.pbs.middleware.server.features.notification.email.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pbs.middleware.server.features.notification.email.configuration.TemplateType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Builder
@Data
public class Email {

    @Singular("to")
    private final List<String> to;

    @Singular("cc")
    private final List<String> cc;

    @Singular("bcc")
    private final List<String> bcc;

    private final String subject;

    private final Map<String, Object> parameters;

    @Singular("attachment")
    private final List<Attachment> attachments;

    private final TemplateType templateType;

    @JsonIgnore
    public String getEmails() {
        if (to.size() == 1) {
            return to.get(0);
        }
        return to.stream().collect(Collectors.joining(",","[", "]"));
    }

}
