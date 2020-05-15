package com.pbs.middleware.server.features.template.service;

import com.pbs.middleware.server.common.domain.Id;
import com.pbs.middleware.server.common.storage.EventRepository;
import com.pbs.middleware.server.features.pbs.PbsCommandFactory;
import com.pbs.middleware.api.template.TemplateProperties;
import com.pbs.middleware.api.template.CreateTemplate;
import com.pbs.middleware.api.template.UpdateTemplate;
import com.pbs.middleware.server.features.template.events.TemplateEvent;
import com.pbs.middleware.server.features.template.storage.TemplateFactory;
import io.micrometer.core.instrument.util.StringUtils;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.pbs.middleware.server.features.pbs.service.Mapper.mapToJobConfiguration;
import static com.pbs.middleware.server.features.pbs.service.Mapper.mapToQsubParameters;

@Service
@Validated
@RequiredArgsConstructor
public class TemplateService {

    private static final Logger log = LoggerFactory.getLogger(TemplateService.class);

    @NonNull
    private final TemplateFactory factory;

    @NonNull
    private final EventRepository<TemplateEvent, String> repository;

    public Set<String> getAll() {
        return repository.findAllIds().stream().map(Id::getDomainId).collect(Collectors.toSet());
    }

    public void create(@Valid CreateTemplate dto) {
        String templateId = dto.getTemplateId();
        if (StringUtils.isBlank(templateId)) {
            //todo better exception for UI
            throw new IllegalArgumentException("Template id can not be blank!");
        }
        log.info("Creating new template {}", templateId);
        factory.create(templateId).create(
                dto.getDescription(),
                dto.getGroup(),
                dto.getConnection(),
                dto.getHandleStatusScript(),
                mapToQsubParameters(dto));
    }

    public void update(
            String templateId,
            @Valid UpdateTemplate dto) {
        log.info("Updating template {}", templateId);
        factory.get(templateId) // TODO validate
                .update(
                        dto.getDescription(),
                        dto.getGroup(),
                        dto.getConnection(),
                        dto.getHandleStatusScript(),
                        mapToQsubParameters(dto));
    }

    public com.pbs.middleware.server.features.template.domain.Template get(@NonNull String templateId) {
        return factory.get(templateId);
    }

    public com.pbs.middleware.server.features.template.domain.Template getTemplate(@NonNull String templateId) {
        return factory.get(templateId);
    }

    public String getQsub(@NonNull String templateId) {
        return getQsub(factory.get(templateId));
    }

    public String getQsub(@NonNull TemplateProperties template) {
        return PbsCommandFactory.qsub().buildToString(mapToJobConfiguration(template));
    }

    public String getQsub(@NonNull com.pbs.middleware.server.features.template.domain.Template template) {
        return PbsCommandFactory.qsub().buildToString(template);
    }

    public String copy(@NonNull String templateId, @NonNull String newTemplateId) {
        com.pbs.middleware.server.features.template.domain.Template template = factory.get(templateId);

        factory.create(newTemplateId)
                .create(
                        template.getDescription(),
                        template.getGroup(),
                        template.getConnection(),
                        template.getScript(),
                        template.getQsubParameters());

        return newTemplateId;
    }

    public void delete(@NonNull String templateId) {
        repository.deleteAllByDomainId(templateId);
    }

}
