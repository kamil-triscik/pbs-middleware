package com.pbs.middleware.server.features.notification.email.service;

import com.pbs.middleware.server.common.utils.Optional;
import com.pbs.middleware.server.features.notification.email.configuration.TemplateType;
import com.pbs.middleware.server.features.notification.email.domain.Email;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@ConditionalOnProperty({
        "middleware.server.mail.host",
        "middleware.server.mail.port",
        "middleware.server.mail.username",
        "middleware.server.mail.password",
        "middleware.server.mail.from"
})
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfiguration;

    public void send(String email, String subject,  String text) {
        send(Set.of(email), subject, text);
    }

    public void send(Set<String> emails, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emails.toArray(new String[0]));
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
        System.out.println();
    }

    public void send(Email email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            setupMessage(message, email);
            mailSender.send(message);
            log.info("Email sent to " + email.getTo());
        } catch (MessagingException e) {
            log.error("Email sending failed", e);
        }
    }

    private void setupMessage(MimeMessage message, Email email) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        EmailBody body = getEmailBody(email.getParameters(), email.getTemplateType());

        helper.setTo(email.getTo().toArray(new String[0]));
        helper.setCc(email.getCc().toArray(new String[0]));
        helper.setBcc(email.getBcc().toArray(new String[0]));
        helper.setText(body.text, body.isHtml);
        helper.setSubject(email.getSubject());

        attachLogoFile(helper);
        addAttachments(helper, email);
    }

    private EmailBody getEmailBody(Map<String, Object> parameters, TemplateType templateType) {
        return Optional.of(templateType)
                .map(this::getTemplate)
                .map(template -> {
                    try {
                        return FreeMarkerTemplateUtils.processTemplateIntoString(template.get(), parameters);
                    } catch (Exception e) {
                        log.error("Problem to generate email html", e);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .map(body -> new EmailBody(body, true))
                .orElse(new EmailBody(templateType.getBackup(parameters), false));
    }

    private Optional<Template> getTemplate(TemplateType templateType) {
        try {
            return Optional.of(freemarkerConfiguration.getTemplate(templateType.getPath()));
        } catch (IOException e) {
            log.error("Template load failed", e);
        }
        return Optional.empty();
    }


    private void addAttachments(MimeMessageHelper helper, Email email) {
        email.getAttachments().forEach(attachment -> {
            try {
                helper.addAttachment(
                        attachment.getFileName(),
                        new ByteArrayResource(attachment.getContent().getBytes()),
                        attachment.getContentType());
            } catch (MessagingException e) {
                log.error("Email attachment binding failed", e);
            }
        });
    }

    private void attachLogoFile(MimeMessageHelper helper) {
        try {
            File logoFile = File.createTempFile("logo", "png");
            FileUtils.copyInputStreamToFile(new ClassPathResource("logo.png").getInputStream(), logoFile);
            helper.addInline("logo.png", logoFile);
        } catch (Exception e) {
            log.error("Email logo loading failed", e);
        }
    }

    @AllArgsConstructor
    private static class EmailBody {

        private String text;

        private boolean isHtml;

    }
}
