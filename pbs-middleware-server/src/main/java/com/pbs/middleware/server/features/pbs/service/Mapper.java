package com.pbs.middleware.server.features.pbs.service;

import com.pbs.middleware.api.job.JobSubmit;
import com.pbs.middleware.server.features.job.utils.JobConfiguration;
import com.pbs.middleware.common.pbs.QsubParameters;
import com.pbs.middleware.common.pbs.Walltime;
import com.pbs.middleware.api.template.Chunk;
import com.pbs.middleware.api.template.Resources;
import com.pbs.middleware.api.template.TemplateProperties;
import com.pbs.middleware.api.template.UpdateTemplate;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class Mapper {

    public static QsubParameters mapToQsubParameters(TemplateProperties dto) {
        QsubParameters parameters = new QsubParameters();

        parameters.setResources(mapResources(dto.getResources()));
        parameters.setVariables(dto.getVariables());
        parameters.setStdout(dto.getStdout());
        parameters.setStderr(dto.getStderr());
        parameters.setPrefix(dto.getQsubPrefix());
        parameters.setScript(dto.getJobScript());
        parameters.setSuffix(dto.getQsubSuffix());
        parameters.setQueue(dto.getQueue());
        parameters.setJobName(dto.getJobName());
        parameters.setWorkdir(dto.getWorkdir());
        parameters.setArguments(dto.getArguments());

        return parameters;
    }

    public static JobConfiguration mapToJobConfiguration(UpdateTemplate dto) {
        JobConfiguration template = new JobConfiguration();

        template.setGroup(dto.getGroup());
        template.setConnection(dto.getConnection());
        template.setScript(dto.getHandleStatusScript());
        template.setQsubParameters(mapToQsubParameters(dto));

        return template;
    }

    public static JobConfiguration mapToJobConfiguration(TemplateProperties dto) {
        JobConfiguration template = new JobConfiguration();
        template.setQsubParameters(mapToQsubParameters(dto));

        return template;
    }

    public static JobConfiguration mapToJobConfiguration(JobSubmit request) {
        JobConfiguration template = new JobConfiguration();

        template.setGroup(request.getGroup());
        template.setConnection(request.getConnection());
        template.setScript(request.getHandleStatusScript());

        QsubParameters parameters = new QsubParameters();

        parameters.setResources(request.getResources());
        parameters.setVariables(request.getVariables());
        parameters.setStdout(request.getStdout());
        parameters.setStderr(request.getStderr());
        parameters.setPrefix(request.getQsubPrefix());
        parameters.setScript(request.getJobScript());
        parameters.setSuffix(request.getQsubSuffix());
        parameters.setQueue(request.getQueue());
        parameters.setJobName(request.getJobName());
        parameters.setWorkdir(request.getWorkdir());
        parameters.setArguments(request.getArguments());
        template.setQsubParameters(parameters);

        return template;
    }

    private static com.pbs.middleware.common.pbs.Resources mapResources(Resources dto) {
        if (dto == null) {
            return null;
        }

        com.pbs.middleware.common.pbs.Resources resources = new com.pbs.middleware.common.pbs.Resources();

        resources.setWalltime(Walltime.from(dto.getWalltime()));
        resources.setCustomResources(dto.getCustomResources());
        if (dto.getChunks() == null) {
            resources.setChunks(emptyList());
        } else {
            resources.setChunks(
                    dto.getChunks()
                            .stream()
                            .map(Mapper::getChunk)
                            .collect(Collectors.toList())
            );
        }

        return resources;
    }

    public static Resources mapResources(com.pbs.middleware.common.pbs.Resources resources) {
        if (resources == null) {
            return null;
        }
        Resources dto = new Resources();

        dto.setWalltime(resources.getWalltime().toString());
        dto.setCustomResources(resources.getCustomResources());
        dto.setChunks(
                resources.getChunks()
                        .stream()
                        .map(Mapper::getChunk)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    private static com.pbs.middleware.common.pbs.Chunk getChunk(Chunk dto) {
        return com.pbs.middleware.common.pbs.Chunk.builder()
                .id(dto.getId())
                .count(dto.getCount())
                .ncpus(dto.getNcpus())
                .mem(dto.getMem())
                .customResources(dto.getCustomResources())
                .build();
    }

    private static Chunk getChunk(com.pbs.middleware.common.pbs.Chunk chunk) {
        Chunk dto = new Chunk();

        dto.setId(chunk.getId());
        dto.setCount(chunk.getCount());
        dto.setNcpus(chunk.getNcpus());
        dto.setMem(chunk.getMem());
        dto.setCustomResources(chunk.getCustomResources());

        return dto;
    }

}
