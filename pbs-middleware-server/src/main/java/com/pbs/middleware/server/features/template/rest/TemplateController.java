package com.pbs.middleware.server.features.template.rest;

import com.pbs.middleware.api.template.TemplateProperties;
import com.pbs.middleware.api.template.CreateTemplate;
import com.pbs.middleware.api.template.Template;
import com.pbs.middleware.api.template.UpdateTemplate;
import com.pbs.middleware.server.features.template.service.TemplateMapper;
import com.pbs.middleware.server.features.template.service.TemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Set;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.pbs.middleware.server.common.security.SecurityConfig.ALLOW_ADMIN;
import static com.pbs.middleware.server.features.documentation.TagsConfig.TEMPLATES_TAG;
import static com.pbs.middleware.api.template.ApiConfig.TEMPLATES;
import static com.pbs.middleware.api.template.ApiConfig.TEMPLATES_COPY;
import static com.pbs.middleware.api.template.ApiConfig.TEMPLATES_CREATE;
import static com.pbs.middleware.api.template.ApiConfig.TEMPLATES_DELETE;
import static com.pbs.middleware.api.template.ApiConfig.TEMPLATES_GET;
import static com.pbs.middleware.api.template.ApiConfig.TEMPLATES_GET_ALL;
import static com.pbs.middleware.api.template.ApiConfig.TEMPLATES_UPDATE;
import static com.pbs.middleware.api.template.ApiConfig.TEMPLATES_EXISTS;
import static com.pbs.middleware.api.template.ApiConfig.TEMPLATES_QSUB;
import static com.pbs.middleware.api.template.ApiConfig.TEMPLATES_QSUB_ID;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Api(tags = TEMPLATES_TAG)
@RequestMapping(TEMPLATES)
@RequiredArgsConstructor
public class
TemplateController {

    @NonNull
    private final TemplateService service;

    @NonNull
    private final TemplateMapper mapper;

    @ApiOperation(value = "Retrieve all configured templates")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all templates IDs"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(
            value = TEMPLATES_GET_ALL,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<String>> getTemplates() {
        return ok(service.getAll());
    }

    @ApiOperation(value = "Create new template")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "New template created!"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 409, message = "Template already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(
            value = TEMPLATES_CREATE,
            consumes = APPLICATION_JSON_VALUE,
            produces = {TEXT_PLAIN_VALUE, APPLICATION_JSON_VALUE})
    public ResponseEntity<String> create(@RequestBody CreateTemplate body) {
        service.create(body);
        return ok(body.getTemplateId());
    }

    @ApiOperation(value = "Update template")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Template updated!"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Template not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping(
            value = TEMPLATES_UPDATE,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @ApiParam(value = "Template unique ID", required = true)
            @PathVariable("templateId") String templateId,
            @ApiParam(value = "Template parameters", required = true)
            @RequestBody UpdateTemplate body) {
        service.update(templateId, body);
        return ok().build();
    }

    @ApiOperation(value = "Fetch template")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Template fetched!"),
            @ApiResponse(code = 404, message = "Template not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(
            value = TEMPLATES_GET,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Template> get(
            @ApiParam(value = "Template unique ID", required = true)
            @PathVariable("templateId") String templateId) {
        return ok(mapper.mapToDto(service.get(templateId)));
    }

    @ApiOperation(value = "Check if template with provided id exists")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Result, if template with provided id exists", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(
            value = TEMPLATES_EXISTS,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> exists(@ApiParam(value = "Template unique ID", required = true)
                                          @PathVariable("templateId") String templateId) {
        return ok(service.getAll().contains(templateId));
    }

    @ApiOperation(value = "Return how qsub command will looks like for specified template")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Qsub command"),
            @ApiResponse(code = 404, message = "Template not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = TEMPLATES_QSUB_ID)
    public ResponseEntity<String> getQsubPost(@PathVariable("templateId") String templateId) {
        return ok(service.getQsub(templateId));
    }

    @ApiOperation(value = "Return how qsub command will looks like for specified template")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Qsub command"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = TEMPLATES_QSUB)
    public ResponseEntity<String> getQsubPost(@Valid @RequestBody TemplateProperties body) {
        return ok(service.getQsub(body));
    }

    @ApiOperation(value = "Copy template")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Template copied!"),
            @ApiResponse(code = 404, message = "Template not found"),
            @ApiResponse(code = 409, message = "Template already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(
            value = TEMPLATES_COPY,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> copy(
            @ApiParam(value = "Template unique ID", required = true)
            @PathVariable("templateId") String templateId,
            @ApiParam(value = "New template unique ID", required = true)
            @PathVariable(value = "newId") String newId) {
        return ok(service.copy(templateId, newId));
    }

    @PreAuthorize(ALLOW_ADMIN)
    @ApiOperation(value = "Delete template")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Template fetched!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping(
            value = TEMPLATES_DELETE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(
            @ApiParam(value = "Template unique ID", required = true)
            @PathVariable("templateId") String templateId) {
        service.delete(templateId);
        return ok().build();
    }
}
