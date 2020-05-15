package com.pbs.middleware.api.upload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ApiModel(value = "File", description = "DTO representing file, which should be uploaded.")
public class File {

    @ApiModelProperty(name = "name", value = "File name")
    private final String name;

    @ApiModelProperty(name = "size", value = "File size in bytes")
    private final Long size;

    @ApiModelProperty(name = "hash", value = "File hash")
    private final String hash;

    @ApiModelProperty(name = "state", value = "Files upload state", allowableValues = "NOT_PROVIDED, IN_PROGRESS, UPLOADED, FAILED, CANCELLED, UNKNOWN")
    private final FileUploadState state;

}
