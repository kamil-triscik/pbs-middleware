package com.pbs.middleware.server.features.job.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when notification should be send.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Job")
public class JobNotificationRequestEvent extends JobEvent {

    private UUID owner;

    private String recipient;

    private String subject;

    private String body;

    public JobNotificationRequestEvent(UUID domainId, UUID owner, String recipient, String subject, String body) {
        super(domainId);
        this.owner = owner;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    public static JobNotificationRequestEvent monitoringNotStarted(UUID domainId, UUID owner, String jobId, Exception e) {
        return new JobNotificationRequestEvent(
                domainId,
                owner,
                null,
                "Job launched but monitoring not started",
                "Job launched with id " + jobId + " monitoring probably not started due to\n\n\n" + ExceptionUtils.getStackTrace(e));
    }

    public static JobNotificationRequestEvent of(UUID jobId, String subject, String body) {
        return new JobNotificationRequestEvent(jobId, null, null, subject, body);
    }

    public static JobNotificationRequestEvent of(UUID jobId, String recipient, String subject, String body) {
        return new JobNotificationRequestEvent(jobId, null, recipient, subject, body);
    }

    @Override
    public String toString() {
        return (owner != null ? " for " + owner : " ") +
                (recipient != null ? " for " + recipient : " ") +
                " about " + subject;
    }


}
