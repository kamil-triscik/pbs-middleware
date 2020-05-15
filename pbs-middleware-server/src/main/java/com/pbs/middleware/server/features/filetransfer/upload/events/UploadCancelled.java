package com.pbs.middleware.server.features.filetransfer.upload.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when client decide to cancel data upload.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Upload")
public class UploadCancelled extends UploadEvent {

    /**
     * Reason what upload was cancelled. Optional.
     */
    private String reason;

    /**
     * When a client decide to cancel uploading data to files, some files could be already uploaded.
     * If this flag is true, already uploaded files will be removed from remote server.
     */
    private Boolean removeUploadedFiles;

    public UploadCancelled(UUID domainId, String reason, Boolean removeUploadedFiles) {
        super(domainId);
        this.reason = reason;
        this.removeUploadedFiles = removeUploadedFiles;
    }
}
