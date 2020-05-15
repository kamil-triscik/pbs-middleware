package com.pbs.middleware.server.features.filetransfer.download.status.repository;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDownloadStatusId implements Serializable {

    private UUID id;

    private DownloadStatus downloadStatus;

}
