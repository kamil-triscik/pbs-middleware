package com.pbs.middleware.server.features.template.service;

import com.pbs.middleware.server.features.job.utils.JobConfiguration;
import com.pbs.middleware.common.pbs.QsubParameters;
import com.pbs.middleware.server.features.pbs.service.Mapper;
import com.pbs.middleware.api.template.Template;
import org.springframework.stereotype.Component;

@Component
public class TemplateMapper {

    public Template mapToDto(com.pbs.middleware.server.features.template.domain.Template template) {
        return mapToDto(JobConfiguration.of(template));
    }

    public Template mapToDto(JobConfiguration props) {
        Template dto = new Template();

        dto.setTemplateId(props.getTemplateId());
        dto.setDescription(props.getDescription());
        dto.setGroup(props.getGroup());
        dto.setConnection(props.getConnection());
        dto.setHandleStatusScript(props.getScript());

        QsubParameters params = props.getQsubParameters();
        dto.setResources(Mapper.mapResources(params.getResources()));
        dto.setVariables(params.getVariables());
        dto.setStdout(params.getStdout());
        dto.setStderr(params.getStderr());
        dto.setQsubPrefix(params.getPrefix());
        dto.setJobScript(params.getScript());
        dto.setQsubSuffix(params.getSuffix());
        dto.setQueue(params.getQueue());
        dto.setJobName(params.getJobName());
        dto.setWorkdir(params.getWorkdir());
        dto.setArguments(params.getArguments());

        return dto;

    }

}
