package com.pbs.middleware.server.features.job.email;

import com.pbs.middleware.server.features.notification.email.configuration.TemplateType;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class JobEmailParams {

    private String recipient;

    private String subject;

    private TemplateType templateType;

    private Map<String, Object> parameters;

}
