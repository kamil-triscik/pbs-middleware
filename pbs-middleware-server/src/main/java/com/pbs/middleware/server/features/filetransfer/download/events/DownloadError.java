package com.pbs.middleware.server.features.filetransfer.download.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when some error occurred.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Download")
public class DownloadError extends DownloadEvent {

    private String message;

    private String exception;

    public DownloadError(UUID domainId, String message, String exception) {
        super(domainId);
        this.message = message;
        this.exception = exception;
    }

}
