package com.pbs.middleware.api.job;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "KillJobRequest", description = "DTO for killing running job request!")
public class KillJobRequest {

    @ApiModelProperty(name = "exitCode", value = "Custom exit code for failed job.")
    private Integer exitCode;

    @ApiModelProperty(name = "message", value = "In case user want provide some details, why job is killed.")
    private String reason;

}
