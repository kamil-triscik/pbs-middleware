package com.pbs.middleware.server.features.filetransfer.download.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when email notification sending failed.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Download")
public class EmailSentFailed extends DownloadEvent {

    private final String error;

    public EmailSentFailed(UUID domainId, String error) {
        super(domainId);
        this.error = error;
    }

}
