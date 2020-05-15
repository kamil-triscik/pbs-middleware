package com.pbs.middleware.server.features.filetransfer.upload.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when file upload failed.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Upload")
public class FileUploadFailed extends UploadEvent {

    /**
     * Name of file.
     */
    private String filename;

    /**
     * Error occurred during upload.
     */
    private String error;

    public FileUploadFailed(UUID domainId, String filename, String error) {
        super(domainId);
        this.filename = filename;
        this.error = error;
    }
}
