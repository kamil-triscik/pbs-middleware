package com.pbs.middleware.api.contact;

import com.pbs.middleware.common.validations.ValidStringCollection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "CreateContact", description = "Contact DTO for create request")
public class CreateContact {

    //unique
    @ApiModelProperty(name = "name", value = "Contact name")
    private String name;

    @Email
    @NotBlank(message = "Contact name cannot be empty.")
    @Size(max = 50, message = "Contact email cannot be longer than 20 characters.")
    @ApiModelProperty(name = "id", value = "Contact identification")
    private String email;

    @Size(max = 100, message = "Connection description cannot be longer than 100 characters.")
    @ApiModelProperty(name = "email", value = "Contact description")
    private String description;


    @ValidStringCollection(fieldName = "contactTypes")
    @NotNull(message = "Contact types list can not be null")
    @NotEmpty(message = "Contact types list can not be empty")
    @ApiModelProperty(
            name = "contactTypes",
            value = "Contact types",
            allowableValues = "JOB, UPLOAD, DOWNLOAD, SUPPORT",
            example = "[UPLOAD, DOWNLOAD]")
    private Set<String> contactTypes;
}
