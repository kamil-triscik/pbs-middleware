package com.pbs.middleware.api.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "UpdateTemplate", description = "Template DTO for update request")
public class UpdateTemplate extends TemplateProperties {

    @ApiModelProperty(name = "description", value = "Template description")
    private String description;

    @ApiModelProperty(name = "description", value = "Template description")
    //    @NotEmptyString(message = "Group can not be blank")
    private String group;

    //    @ConnectionExists
    @ApiModelProperty(name = "connection", value = "Connection used for job related operations")
    @NotBlank(message = "Connection is mandatory")
    private String connection;

    //    @ScriptExists
//    @NotEmptyString(message = "Handle status script can not be blank")
    @ApiModelProperty(name = "handleStatusScript", value = "Script handling job failures")
    private String handleStatusScript;

}
