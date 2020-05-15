package com.pbs.middleware.server.features.filetransfer.download.notification;

import com.pbs.middleware.server.features.contact.repository.Contact;
import com.pbs.middleware.server.features.contact.service.ContactService;
import com.pbs.middleware.server.features.filetransfer.download.domain.Download;
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

import static com.pbs.middleware.server.features.contact.repository.ContactType.DOWNLOAD;
import static com.pbs.middleware.server.features.notification.email.configuration.TemplateType.DOWNLOAD_ADMIN_FAIL;

@Component
@RequiredArgsConstructor
public class DownloadEmailFactory {

    private final ContactService contactService;

    public Optional<Email> get(List<Download> downloads) {
        return get(downloads, new HashMap<>());
    }

    public Optional<Email> get(List<Download> downloads, Map<UUID, Map<String, String>> currentExceptions) {
        Map<String, Object> params = new HashMap<>();
        List<DownloadEmailParams> emailsParams = downloads
                .stream()
                .map(DownloadEmailParamsFactory::getParams)
                .collect(Collectors.toList());

        params.put("failures", emailsParams.stream()
                .collect(Collectors.toMap(
                        it -> it.getDownloadId().toString(),
                        DownloadEmailParams::getParameters
                )));
        params.put("count", downloads.size());

        Map<UUID, Map<String, String>> origExceptions = emailsParams.stream()
                .collect(Collectors.toMap(DownloadEmailParams::getDownloadId, DownloadEmailParams::getExceptions));

        List<Attachment> attachments = new LinkedList<>();
        origExceptions.keySet().forEach(download -> {
            origExceptions.get(download).keySet().forEach(filename -> {
                String exception = currentExceptions
                        .getOrDefault(download, origExceptions.get(download))
                        .getOrDefault(filename, origExceptions.get(download).get(filename));
                attachments.add(new Attachment(download + "-" + filename + "-stacktrace.txt", exception, "text/plain"));
            });
        });

        Set<String> downloadAdmins = contactService.findByType(DOWNLOAD).stream().map(Contact::getEmail).collect(Collectors.toSet());
        if (downloadAdmins.isEmpty()) {
            downloadAdmins = contactService.getAdminEmails();
        }

        return Optional.of(Email.builder()
                .to(downloadAdmins)
                .subject("Download failure")
                .parameters(params)
                .attachments(attachments)
                .templateType(DOWNLOAD_ADMIN_FAIL)
                .build());
    }

    public Optional<Email> get(Download download) {
        DownloadEmailParams emailParams = DownloadEmailParamsFactory.getParams(download);
        if (emailParams.getRecipient() == null || emailParams.getRecipient().isEmpty()) {
            return Optional.empty();
        }
        Map<String, String> exceptions = emailParams.getExceptions();

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
