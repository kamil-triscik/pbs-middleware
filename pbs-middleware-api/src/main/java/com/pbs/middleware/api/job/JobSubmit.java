package com.pbs.middleware.api.job;

import com.pbs.middleware.common.pbs.Resources;
import com.pbs.middleware.common.validations.NotEmptyString;
import com.pbs.middleware.common.validations.ValidCollection;
import com.pbs.middleware.common.validations.ValidMap;
import com.pbs.middleware.common.validations.ValidStringCollection;
import com.pbs.middleware.common.validations.ValidStringMap;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.Builder;
import lombok.Data;

@Data
//@ConnectionMandatory
//@ScriptMandatory
@Builder
@ApiModel(value = "JobSubmit", description = "Job DTO for submitting new job request")
public class JobSubmit {

//    @TemplateExist
    @NotEmptyString(message = "Template id can not be empty")
    @ApiModelProperty(name = "templateId", value = "Template ID.")
    private String templateId;

    @Valid
    @ApiModelProperty(name = "resources", value = "Required Job resources.")
    private Resources resources;

    @NotEmptyString
    @ApiModelProperty(name = "queue", value = "PBS queue.")
    private String queue;

    @ValidMap(fieldName = "variables")
    @ValidStringMap(fieldName = "variables")
    @ApiModelProperty(name = "variables", value = "Job script variables map.")
    private Map<String, String> variables;

    @NotEmptyString
    @ApiModelProperty(name = "workdir", value = "PBS script working directory.")
    private String workdir;

    @NotEmptyString
    @ApiModelProperty(name= "qsubPrefix", value = "PBS qsub prefix.")
    private String qsubPrefix;

    @NotEmptyString(message = "Job script is mandatory")
    @ApiModelProperty(name = "jobScript", value = "PBS script. Mandatory if templateId is null!")
    private String jobScript;

    @NotEmptyString
    @ApiModelProperty(name = "qsubSuffix", value = "PBS qsub command postfix.")
    private String qsubSuffix;

    @NotEmptyString
    @ApiModelProperty(name = "stdout", value = "PBS job standard output log file.")
    private String stdout;

    @NotEmptyString
    @ApiModelProperty(name = "stderr", value = "PBS job error output log file.")
    private String stderr;

    @ValidCollection(fieldName = "arguments")
    @ValidStringCollection(fieldName = "arguments")
    @ApiModelProperty(name = "arguments", value = "Job script arguments")
    private List<String> arguments;

    @NotEmptyString(message = "Group can not be blank")
    @ApiModelProperty(name = "group", value = "Job group", dataType = "java.lang.String", allowEmptyValue = true)
    private String group;

    @ApiModelProperty(name = "jobName", value = "Custom job name.")
    private String jobName;

//    @ConnectionExists
    @NotEmptyString(message = "Connection can not be empty")
    @ApiModelProperty(name = "connection", value = "Job connection. Mandatory if templateId is null!")
    private String connection;

//    @ScriptExists
    @NotEmptyString(message = "Handle status script can not be blank")
    @ApiModelProperty(name = "handleStatusScript", value = "Job error handling script!")
    private String handleStatusScript;

}
