package com.pbs.middleware.api.script;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "UpdateScript", description = "Script DTO for update request")
public class UpdateScript {

    @NotBlank(message = "Script name cannot be empty.")
    @Size(max = 20, message = "Script name cannot be longer than 20 characters.")
    @ApiModelProperty(name = "name", value = "Script name")
    private String name;

    @Size(max = 100, message = "Description name cannot be longer than 100 characters.")
    @ApiModelProperty(name = "description", value = "Script description")
    private String description;

    @NotBlank(message = "Code cannot be empty.")
    @ApiModelProperty(name = "code", value = "Script code")
    private String code;
}
