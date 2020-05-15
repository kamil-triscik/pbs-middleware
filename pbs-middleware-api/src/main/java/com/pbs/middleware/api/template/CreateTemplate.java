package com.pbs.middleware.api.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "CreateTemplate", description = "Template DTO for update request")
public class CreateTemplate extends UpdateTemplate {

    @NotBlank(message = "Template id is mandatory")
    @ApiModelProperty(name = "templateId", value = "Template identification")
    private String templateId;

}
