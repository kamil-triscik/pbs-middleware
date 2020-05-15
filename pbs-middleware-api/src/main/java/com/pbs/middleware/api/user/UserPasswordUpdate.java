package com.pbs.middleware.api.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "UserPasswordUpdate", description = "User DTO for credentials update request")
public class UserPasswordUpdate {

    @ApiModelProperty(name = "oldPassword", value = "User old password")
    private String oldPassword;

    @ApiModelProperty(name = "newPassword", value = "User new password")
    private String newPassword;

}
