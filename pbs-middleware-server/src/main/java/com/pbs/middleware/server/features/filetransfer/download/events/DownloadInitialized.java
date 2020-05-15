package com.pbs.middleware.server.features.filetransfer.download.events;

import java.util.Set;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when client ask for download data from the remote server.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Download")
public class DownloadInitialized extends DownloadEvent {

    private String connection;

    private String description;

    private String email;

    private String folder;

    private Set<String> files;

    private Boolean remove;

    public DownloadInitialized(
            UUID domainId,
            String connection,
            String description,
            String email,
            String folder,
            Set<String> files,
            Boolean remove) {
        super(domainId);
        this.connection = connection;
        this.description = description;
        this.email = email;
        this.folder = folder;
        this.files = files;
        this.remove = remove;
    }
}
