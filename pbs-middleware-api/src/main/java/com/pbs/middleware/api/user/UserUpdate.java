package com.pbs.middleware.api.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "UserUpdate", description = "User DTO for update request")
public class UserUpdate extends UserBaseDto {

    @ApiModelProperty(name = "disabled", value = "Flag if user is disabled or enabled")
    private Boolean disabled;

}
