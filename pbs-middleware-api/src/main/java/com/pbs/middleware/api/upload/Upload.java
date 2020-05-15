package com.pbs.middleware.api.upload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
@ApiModel(value = "Upload", description = "Upload DTO for readonly purposes.")
public class Upload {

    @ApiModelProperty(name = "id", value = "Upload identification")
    private UUID id;

    @ApiModelProperty(name = "connection", value = "Connection used for uploading data to target server")
    private String connection;

    @ApiModelProperty(name = "destination", value = "Upload destination")
    private String destination;

    @ApiModelProperty(name = "state", value = "Upload state", allowableValues = "QUEUED,IN_PROGRESS,UPLOADED,FAILED,CANCELLED,UNKNOWN")
    private UploadState state;

    @ApiModelProperty(name = "description", value = "Upload description")
    private String description;

    @ApiModelProperty(name = "files", value = "Collection of uploaded files")
    private List<File> files;

}
