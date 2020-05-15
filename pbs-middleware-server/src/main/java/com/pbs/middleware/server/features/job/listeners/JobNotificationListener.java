package com.pbs.middleware.server.features.job.listeners;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import com.pbs.middleware.server.features.job.events.JobNotificationRequestEvent;
import com.pbs.middleware.server.features.notification.email.service.EmailNotifier;
import com.pbs.middleware.server.features.users.repository.User;
import com.pbs.middleware.server.features.users.service.UserService;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class JobNotificationListener {

    @NonNull
    private final EmailNotifier emailNotifier;

    @NonNull
    private final UserService userService;

    @EventListener
    public void onApplicationEvent(final JobNotificationRequestEvent event) throws MiddlewareException {
        log.info("Preparing notification request " + event.toString());
        new Thread(() -> {
            sendNotification(event);
        }).start();
    }

    private void sendNotification(JobNotificationRequestEvent notificationRequest) {
        getRecipient(notificationRequest)
                .ifPresentOrElse(
                        recipient -> emailNotifier.send(recipient, notificationRequest.getSubject(), notificationRequest.getBody()),
                        () -> emailNotifier.sendJobNotification(notificationRequest.getSubject(), notificationRequest.getBody()));

    }

    private Optional<String> getRecipient(JobNotificationRequestEvent notificationRequest) {
        if (notificationRequest.getRecipient() != null && !notificationRequest.getRecipient().isEmpty()) {
            return Optional.of(notificationRequest.getRecipient());
        }
        if (notificationRequest.getOwner() != null) {
            return userService.findById(notificationRequest.getOwner()).map(User::getEmail);
        }
        return Optional.empty();
    }
}
