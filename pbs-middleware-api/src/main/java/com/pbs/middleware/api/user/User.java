package com.pbs.middleware.api.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "User", description = "User DTO for readonly purposes")
public class User extends UserUpdate {

    @ApiModelProperty(name = "id", value = "User identification")
    private UUID id;

    @ApiModelProperty(name = "username", value = "Username")
    private String username;

    @ApiModelProperty(name = "role", value = "User role")
    private String role;
}