package com.pbs.middleware.server.features.filetransfer.upload.events;

import com.pbs.middleware.server.features.filetransfer.upload.domain.FileOptions;
import java.util.List;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when client ask for uploading new data to the remote server.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Upload")
public class UploadInitialized extends UploadEvent {

    /**
     * Destination in remote server, where data should be upload.
     */
    private String destination;

    /**
     * Name of a prepared connection, which should be used for connection to the remote server.
     */
    private String connection;

    /**
     * A client email for case the client want to be notified after upload is finish or failed. Optional.
     */
    private String email;

    /**
     * Upload description. Optional.
     */
    private String description;

    /**
     * List of files, which should be uploaded to the remote server.
     */
    private List<FileOptions> files;

    public UploadInitialized(UUID domainId, String destination, String connection, String email, String description, List<FileOptions> files) {
        super(domainId);
        this.destination = destination;
        this.connection = connection;
        this.email = email;
        this.description = description;
        this.files = files;
    }
}
