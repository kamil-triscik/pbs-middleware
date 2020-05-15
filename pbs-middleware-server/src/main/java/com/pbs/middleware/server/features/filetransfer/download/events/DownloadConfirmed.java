package com.pbs.middleware.server.features.filetransfer.download.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when client confirm, that all files were downloaded.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Download")
public class DownloadConfirmed extends DownloadEvent {

    public DownloadConfirmed(UUID domainId) {
        super(domainId);
    }

}
