package com.pbs.middleware.api.download;

import com.pbs.middleware.common.validations.NotEmptyString;
import com.pbs.middleware.common.validations.ValidCollection;
import com.pbs.middleware.common.validations.ValidStringCollection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "DownloadRequest", description = "Download request DTO")
public class DownloadRequest {

    @Size(max = 150, message = "Download description name cannot be longer than 150 characters.")
    @ApiModelProperty(name = "description", value = "Download description")
    private String description;

    @NotBlank(message = "Connection is mandatory")
    @ApiModelProperty(name = "connection", value = "Connection used for downloading data from source server")
    private String connection;

    @NotEmptyString(message = "Folder can not be empty")
    @ApiModelProperty(name = "folder", value = "Source folder on source server")
    private String folder;

    @ValidStringCollection(fieldName = "files")
    @ValidCollection(fieldName = "files")
    @ApiModelProperty(name = "files", value = "Collection of files download in this download")
    private Set<String> files;

    @Email
    @NotEmptyString
    @ApiModelProperty(name = "email", value = "Client can specify email recipient otherwise logged user email will be used.")
    private String email = null;

    @ApiModelProperty(name = "notify", value = "Flag if client want to be notified about download progress.")
    private Boolean notify;

    @ApiModelProperty(name = "remove", value = "Flag if files on source server should be removed after successful download")
    private Boolean remove;

}
