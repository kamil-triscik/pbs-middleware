package com.pbs.middleware.server.features.filetransfer.download.events;

import com.pbs.middleware.server.common.domain.MiddlewareEvent;
import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Download")
public abstract class DownloadEvent extends MiddlewareEvent<UUID> {

    public DownloadEvent(UUID domainId) {
        super(domainId);
    }

}
