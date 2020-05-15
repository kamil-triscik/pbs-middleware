package com.pbs.middleware.server.features.script.service;

import com.pbs.middleware.api.script.CreateScript;
import com.pbs.middleware.api.script.Script;
import com.pbs.middleware.api.script.UpdateScript;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ScriptMapper {

    public Script mapToDto(com.pbs.middleware.server.features.script.repository.Script script) {
        Script dto = new Script();

        dto.setId(script.getId());
        dto.setName(script.getName());
        dto.setDescription(script.getDescription());
        dto.setCode(script.getCode());

        return dto;
    }

    public com.pbs.middleware.server.features.script.repository.Script mapFromDto(CreateScript createDto) {
        com.pbs.middleware.server.features.script.repository.Script script = new com.pbs.middleware.server.features.script.repository.Script();

        script.setName(createDto.getName());
        script.setDescription(createDto.getDescription());
        script.setCode(createDto.getCode());

        return script;
    }

    public com.pbs.middleware.server.features.script.repository.Script mapFromDto(UpdateScript updateDto) {
        return mapFromDto(null, updateDto);
    }

    public com.pbs.middleware.server.features.script.repository.Script mapFromDto(UUID id, UpdateScript updateDto) {
        com.pbs.middleware.server.features.script.repository.Script script = new com.pbs.middleware.server.features.script.repository.Script();

        if (id != null) {
            script.setId(id);
        }
        script.setName(updateDto.getName());
        script.setDescription(updateDto.getDescription());
        script.setCode(updateDto.getCode());

        return script;
    }

}
