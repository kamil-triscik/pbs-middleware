package com.pbs.middleware.server.features.filetransfer.upload.listeners;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadFinished;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploadFailed;
import com.pbs.middleware.server.features.filetransfer.upload.notification.UploadEmailFactory;
import com.pbs.middleware.server.features.notification.email.service.EmailService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadNotifierListener {

    @NonNull
    private final EmailService emailService;

    @NonNull
    private final UploadEmailFactory emailFactory;

    @EventListener
    public void onEvent(final FileUploadFailed event) throws MiddlewareException {
        sendUploadNotification();
    }

    @EventListener
    public void onEvent(final UploadFinished event) throws MiddlewareException {
        sendUploadNotification();
    }

    private void sendUploadNotification() {
//        try {
//            emailFactory.get(upload).ifPresent(email -> {
//                log.info("Sending email notification to " + email.getEmails());
//                emailService.send(email);
//                log.info("Email notification sent!");
//            });
//        } catch (Exception e) {
//            log.error("Email notification processing failed!", e);
//        }
    }
}
