package com.pbs.middleware.server.features.filetransfer.upload.status.repository;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileStatusId implements Serializable {

    private UUID id;

    private UploadStatus uploadStatus;

}
