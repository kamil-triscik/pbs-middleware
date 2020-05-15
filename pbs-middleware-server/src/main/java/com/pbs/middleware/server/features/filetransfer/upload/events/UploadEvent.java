package com.pbs.middleware.server.features.filetransfer.upload.events;

import com.pbs.middleware.server.common.domain.MiddlewareEvent;
import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Parent event for all upload events.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Document(collection = "Upload")
public abstract class UploadEvent extends MiddlewareEvent<UUID> {

    public UploadEvent(UUID domainId) {
        super(domainId);
    }

}
