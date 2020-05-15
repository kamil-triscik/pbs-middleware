package com.pbs.middleware.server.features.filetransfer.download.events;

import java.util.Date;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when file download from remote sever failed.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Download")
public class FileFetchingFailed extends DownloadEvent {

    private String filename;

    private String exception;

    private Long startDate = new Date().getTime();


    public FileFetchingFailed(UUID domainId, String filename, String exception) {
        super(domainId);
        this.filename = filename;
        this.exception = exception;
    }

}
