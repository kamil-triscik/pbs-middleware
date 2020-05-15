package com.pbs.middleware.server.features.filetransfer.upload.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when upload didn't finished successfully.
 * Usually when we were not able to upload data even
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Upload")
public class UploadFailed extends UploadEvent {

    public UploadFailed(UUID domainId) {
        super(domainId);
    }
}
