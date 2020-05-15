package com.pbs.middleware.server.features.filetransfer.upload.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when temporary stored file was deleted from temporary storage.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Upload")
public class TemporaryFileDeleted extends UploadEvent {

    /**
     * ID of file in temporary storage.
     */
    private String tempId;

    public TemporaryFileDeleted(UUID domainId, String tempId) {
        super(domainId);
        this.tempId = tempId;
    }

}
