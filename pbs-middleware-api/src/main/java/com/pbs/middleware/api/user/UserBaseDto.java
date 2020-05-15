package com.pbs.middleware.api.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public abstract class UserBaseDto {

    @NotBlank(message = "First name cannot be empty.")
    @Size(max = 50, message = "First name cannot be longer than 50 characters.")
    @ApiModelProperty(name = "firstName", value = "User first name")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty.")
    @Size(max = 50, message = "Last name cannot be longer than 50 characters.")
    @ApiModelProperty(name = "lastName", value = "User last name")
    private String lastName;

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Email address must be in the valid format.")
    @ApiModelProperty(name = "email", value = "User email")
    private String email;
}
