package com.pbs.middleware.server.features.filetransfer.download.events;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when file was downloaded from remote server.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Download")
public class FileDownloaded extends DownloadEvent {

    private String filename;

    private String path;

    private Long size;

    private String hash;

    private String tempId;

    public FileDownloaded(UUID domainId, String filename, String path, Long size, String hash, String tempId) {
        super(domainId);
        this.filename = filename;
        this.path = path;
        this.size = size;
        this.hash = hash;
        this.tempId = tempId;
    }

}
