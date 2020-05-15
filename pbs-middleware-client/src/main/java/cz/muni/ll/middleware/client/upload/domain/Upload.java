package cz.muni.ll.middleware.client.upload.domain;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class Upload {

    private UUID id;

    private String connection;

    private String destination;

    private String description;

    private List<UploadFile> files;

    private UploadState state;

}
