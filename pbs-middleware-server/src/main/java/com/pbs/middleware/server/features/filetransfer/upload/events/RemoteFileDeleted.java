package com.pbs.middleware.server.features.filetransfer.upload.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used after uploaded file was removed from remote server when client decide to remove whole upload.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Upload")
public class RemoteFileDeleted extends UploadEvent {

    /**
     * Filename of removed file.
     */
    private String filename;

    /**
     * File path on remote server.
     */
    private String remotePath;

    public RemoteFileDeleted(UUID domainId, String filename, String remotePath) {
        super(domainId);
        this.filename = filename;
        this.remotePath = remotePath;
    }
}
