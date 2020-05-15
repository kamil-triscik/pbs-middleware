package com.pbs.middleware.server.features.filetransfer.upload.notification;

import com.pbs.middleware.server.features.contact.repository.Contact;
import com.pbs.middleware.server.features.contact.service.ContactService;
import com.pbs.middleware.server.features.filetransfer.upload.domain.Upload;
import com.pbs.middleware.server.features.notification.email.domain.Attachment;
import com.pbs.middleware.server.features.notification.email.domain.Email;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.features.contact.repository.ContactType.UPLOAD;
import static com.pbs.middleware.server.features.notification.email.configuration.TemplateType.UPLOAD_ADMIN_FAIL;

@Component
@RequiredArgsConstructor
public class UploadEmailFactory {

    private final ContactService contactService;

    public Email get(List<Upload> uploads) {
        return get(uploads, null);
    }

    public Email get(List<Upload> uploads, Map<UUID, Map<String, String>> currentExceptions) {
        Map<String, Object> params = new HashMap<>();
        List<UploadEmailParams> emailsParams = uploads
                .stream()
                .map(UploadEmailParamsFactory::getParams)
                .collect(Collectors.toList());

        params.put("failures", emailsParams.stream()
                .collect(Collectors.toMap(
                        it -> it.getUploadId().toString(),
                        UploadEmailParams::getParameters
                )));
        params.put("count", uploads.size());

        Map<UUID, Map<String, String>> origExceptions = emailsParams.stream()
                .collect(Collectors.toMap(UploadEmailParams::getUploadId, UploadEmailParams::getExceptions));

        List<Attachment> attachments = new LinkedList<>();
        origExceptions.keySet().forEach(upload -> {
            origExceptions.get(upload).keySet().forEach(filename -> {
                String exception = currentExceptions
                        .getOrDefault(upload, origExceptions.get(upload))
                        .getOrDefault(filename, origExceptions.get(upload).get(filename));
                attachments.add(new Attachment(upload + "-" + filename + "-stacktrace.txt", exception, "text/plain"));
            });
        });

        Set<String> uploadAdmins = contactService.findByType(UPLOAD).stream().map(Contact::getEmail).collect(Collectors.toSet());
        if (uploadAdmins.isEmpty()) {
            uploadAdmins = contactService.getAdminEmails();
        }

        return Email.builder()
                .to(uploadAdmins)
                .subject("Upload failure")
                .parameters(params)
                .attachments(attachments)
                .templateType(UPLOAD_ADMIN_FAIL)
                .build();
    }

    public Optional<Email> get(Upload upload) {
        UploadEmailParams emailParams = UploadEmailParamsFactory.getParams(upload);
        if (emailParams.getRecipient() == null || emailParams.getRecipient().isEmpty()) {
            return Optional.empty();
        }
        Map<String, String> exceptions = emailParams.getExceptions();

        if (exceptions.size() == 1) {
            emailParams.getParameters().put("error", exceptions.get(0));
        }

        return Optional.of(Email.builder()
                .attachments(exceptions.keySet().stream()
                        .map(filename -> new Attachment(filename, exceptions.get(filename), "text/plain"))
                        .collect(Collectors.toList()))
                .to(emailParams.getRecipient())
                .subject(emailParams.getSubject())
                .parameters(emailParams.getParameters())
                .templateType(emailParams.getTemplateType())
                .build());
    }
}
