package com.pbs.middleware.api.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "UserCreate", description = "User DTO for create request")
public class UserCreate extends UserBaseDto {

    @NotBlank(message = "Username cannot be empty.")
    @Size(max = 50, message = "Username cannot be longer than 50 characters.")
    @ApiModelProperty(name = "username", value = "Username")
    private String username;

    @NotNull(message = "Exactly one user role must be chosen.")
    @ApiModelProperty(name = "role", value = "User role")
    private String role;
}
