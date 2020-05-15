package com.pbs.middleware.api.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "TemplateProperties", description = "DTO representing template properties.")
public class TemplateProperties {

    @ApiModelProperty
    private Resources resources;

//    @NotEmptyString
    @ApiModelProperty
    private String queue;

    @ApiModelProperty
//    @ValidMap(fieldName = "variables")
//    @ValidStringMap(fieldName = "variables")
    private Map<String, String> variables;

//    @NotEmptyString
    @ApiModelProperty
    private String workdir;

    @ApiModelProperty
    @NotBlank(message = "Job script is mandatory")
    private String jobScript;

//    @NotEmptyString
    private String qsubPrefix;

//    @NotEmptyString
    private String qsubSuffix;

//    @NotEmptyString
    private String stdout;

//    @NotEmptyString
    private String stderr;

//    @ValidCollection(fieldName = "arguments")
//    @ValidStringCollection(fieldName = "arguments")
    private List<String> arguments;

//    @NotEmptyString
    @ApiModelProperty
    private String jobName;

}
