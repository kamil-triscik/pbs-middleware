package com.pbs.middleware.api.template;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "Template", description = "Template DTO for readonly purposes.")
public final class Template extends CreateTemplate {

}
