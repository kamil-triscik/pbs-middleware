package com.pbs.middleware.api.script;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import lombok.Data;

@Data
@ApiModel(value = "Script", description = "Script DTO for readonly purposes.")
public class Script {

    @ApiModelProperty(name = "id", value = "Script identification")
    private UUID id;

    @ApiModelProperty(name = "name", value = "Script name")
    private String name;

    @ApiModelProperty(name = "description", value = "Script description")
    private String description;

    @ApiModelProperty(name = "code", value = "Script code")
    private String code;
}
