package com.pbs.middleware.server.features.filetransfer.upload.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when we started process of uploading file to remote server.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Upload")
public class FileUploadStarted extends UploadEvent {

    /**
     * Name of file which will be upload.
     */
    private final String filename;

    public FileUploadStarted(UUID domainId, String filename) {
        super(domainId);
        this.filename = filename;
    }
}
