package com.pbs.middleware.api.upload;

import com.pbs.middleware.common.validations.NotEmptyString;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.Email;
import lombok.Data;

@Data
@ApiModel(value = "UploadFileRequestDto", description = "Upload DTO for new upload request")
public class UploadRequest {

    @ApiModelProperty(name = "connection", value = "Connection used for uploading data to target server")
    private String connection;

    @ApiModelProperty(name = "destination", value = "Upload destination")
    private String destination;

    @Email
    @NotEmptyString
    @ApiModelProperty(name = "email", value = "Client can specify email recipient otherwise logged user email will be used.")
    private String email = null;

    @ApiModelProperty(name = "notify", value = "Flag if client want to be notified about upload progress.")
    private Boolean notify;

    @ApiModelProperty(name = "description", value = "Upload description")
    private String description;

    @ApiModelProperty(name = "files", value = "Collection of uploaded files")
    private List<UploadFileRequest> files;

}
