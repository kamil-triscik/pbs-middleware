package com.pbs.middleware.server.features.filetransfer.download.events;

import java.util.Set;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when client ask to download whole folder from remote server.
 * In this case, we have fetch list of files from mentioned remote server.
 * This event contain list of files which should be downloaded.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Download")
public class FilesListFetched extends DownloadEvent {

    private Set<String> files;

    public FilesListFetched(UUID domainId, Set<String> files) {
        super(domainId);
        this.files = files;
    }

}
