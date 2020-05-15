package com.pbs.middleware.server.features.filetransfer.download.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when all files are fetched and stored to temporary storage.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Download")
public class AllFilesFetched extends DownloadEvent {

    public AllFilesFetched(UUID domainId) {
        super(domainId);
    }

}
