package com.pbs.middleware.server.features.filetransfer.download.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when file is removed from temporary storage.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Download")
public class FileRemoved extends DownloadEvent {

    private String filename;

    private String id;

    public FileRemoved(UUID domainId, String filename, String id) {
        super(domainId);
        this.filename = filename;
        this.id = id;
    }

}
