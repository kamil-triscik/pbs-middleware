package cz.muni.ll.middleware.client.upload.domain;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadRequest {

    private UUID id;

    private String connection;

    private String destination;

    private String email;

    private Boolean notify;

    private String description;

    private List<UploadFileRequest> files;

}
