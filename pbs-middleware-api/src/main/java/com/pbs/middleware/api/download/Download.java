package com.pbs.middleware.api.download;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
@ApiModel(value = "Download", description = "Download DTO for readonly requests")
public class Download {

    @ApiModelProperty(name = "id", value = "Download identification")
    private UUID id;

    @ApiModelProperty(name = "state", value = "Download state", allowableValues = "IN_PROGRESS, READY, FINISHED, FAILED, CANCELED, UNKNOWN")
    private DownloadState state;

    @ApiModelProperty(name = "connection", value = "Connection used for downloading data from source server")
    private String connection;

    @ApiModelProperty(name = "folder", value = "Source folder on source server")
    private String folder;

    @ApiModelProperty(name = "description", value = "Download description")
    private String description;

    @ApiModelProperty(name = "files", value = "Collection of files download in this download")
    private List<File> files;

}
