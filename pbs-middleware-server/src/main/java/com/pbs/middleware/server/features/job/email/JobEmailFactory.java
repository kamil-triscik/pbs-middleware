package com.pbs.middleware.server.features.job.email;

import com.pbs.middleware.server.features.notification.email.domain.Email;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.features.notification.email.configuration.TemplateType.JOB_NOTIFICATION;

@Component
@RequiredArgsConstructor
public class JobEmailFactory {

    public Email get(String recipient, String subject, Map<String, Object> body) {
        JobEmailParams emailParams = new JobEmailParams(
                recipient,
                subject,
                JOB_NOTIFICATION,
                Map.of("emails", body));

        return Email.builder()
                .to(emailParams.getRecipient())
                .subject(emailParams.getSubject())
                .parameters(emailParams.getParameters())
                .templateType(emailParams.getTemplateType())
                .build();
    }
}
