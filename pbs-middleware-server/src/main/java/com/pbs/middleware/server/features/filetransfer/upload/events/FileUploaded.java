package com.pbs.middleware.server.features.filetransfer.upload.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when file was successfully uploaded to remote server.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Upload")
public class FileUploaded extends UploadEvent {

    /**
     * Name of uploaded file.
     */
    private String filename;

    private Long size;

    public FileUploaded(UUID domainId, String filename, Long size) {
        super(domainId);
        this.filename = filename;
        this.size = size;
    }
}
