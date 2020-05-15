package com.pbs.middleware.server.features.filetransfer.download.listeners;

import com.pbs.middleware.server.features.filetransfer.download.domain.Download;
import com.pbs.middleware.server.features.filetransfer.download.events.AllFilesFetched;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadEvent;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadFailed;
import com.pbs.middleware.server.features.filetransfer.download.events.EmailSent;
import com.pbs.middleware.server.features.filetransfer.download.events.EmailSentFailed;
import com.pbs.middleware.server.features.filetransfer.download.events.PostProcessingFailed;
import com.pbs.middleware.server.features.filetransfer.download.factory.DownloadFactory;
import com.pbs.middleware.server.features.filetransfer.download.notification.DownloadEmailFactory;
import com.pbs.middleware.server.features.notification.email.domain.Email;
import com.pbs.middleware.server.features.notification.email.service.EmailService;
import java.util.ArrayList;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DownloadNotifierListener {

    @NonNull
    private final DownloadFactory downloadFactory;

    @NonNull
    private final DownloadPublisher downloadPublisher;

    @NonNull
    private final EmailService emailService;

    @NonNull
    private final DownloadEmailFactory emailFactory;

    @EventListener
    public void onApplicationEvent(final AllFilesFetched event) {
        sendNotification(event);
    }

    @EventListener
    public void onApplicationEvent(final DownloadFailed event) {
        sendNotification(event);
    }

    @EventListener
    public void onApplicationEvent(final PostProcessingFailed event) {
        sendNotification(event);
    }

    private void sendNotification(DownloadEvent event) {
        try {
            Download download = downloadFactory.get(event.getDomainId());
            if (download.getEmail() != null) {
                Optional<Email> email;
                if (event instanceof PostProcessingFailed) {
                    email = emailFactory.get(new ArrayList<>() {{
                        add(download);
                    }});
                } else {
                    email = emailFactory.get(download);
                }
                email.ifPresent(emailToSend -> {
                    log.info("Sending email notification to {}", emailToSend.getEmails());
                    emailService.send(emailToSend);
                    downloadPublisher.publish(new EmailSent(event.getDomainId()));
                    log.info("Email notification sent!");
                });
            }
        } catch (Exception e) {
            log.error("Email notification sending failed", e);
            downloadPublisher.publish(new EmailSentFailed(event.getDomainId(), e.getLocalizedMessage()));
        }
    }

}
