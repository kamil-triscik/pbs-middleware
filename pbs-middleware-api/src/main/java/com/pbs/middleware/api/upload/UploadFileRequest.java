package com.pbs.middleware.api.upload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UploadFileRequest", description = "DTO for upload file")
public class UploadFileRequest {

    @ApiModelProperty(name = "filename", value = "Filename")
    private final String filename;

    @ApiModelProperty(name = "rename", value = "New filename, in case files should be renamed on target server.")
    private final String rename;

    @ApiModelProperty(name = "extract", value = "In case, uploaded file is in archive file format!", example = "false")
    private final Boolean extract;

    @ApiModelProperty(name = "id", value = "TBD", example = "null")
    private final String secured;
}
