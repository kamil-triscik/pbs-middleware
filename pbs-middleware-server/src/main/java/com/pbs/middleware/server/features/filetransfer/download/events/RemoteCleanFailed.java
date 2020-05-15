package com.pbs.middleware.server.features.filetransfer.download.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when downloaded files were removed. If client ask for it.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Download")
public class RemoteCleanFailed extends DownloadEvent {

    String exception;

    public RemoteCleanFailed(UUID domainId, String exception) {
        super(domainId);
        this.exception = exception;
    }

}
