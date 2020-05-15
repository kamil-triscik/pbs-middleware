package com.pbs.middleware.server.features.script.rest;

import com.pbs.middleware.api.script.CreateScript;
import com.pbs.middleware.api.script.Script;
import com.pbs.middleware.api.script.UpdateScript;
import com.pbs.middleware.server.common.security.SecurityConfig;
import com.pbs.middleware.server.features.documentation.TagsConfig;
import com.pbs.middleware.server.features.script.service.ScriptMapper;
import com.pbs.middleware.server.features.script.service.ScriptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static com.pbs.middleware.api.script.ApiConfig.SCRIPTS;
import static com.pbs.middleware.api.script.ApiConfig.SCRIPTS_CREATE;
import static com.pbs.middleware.api.script.ApiConfig.SCRIPTS_GET;
import static com.pbs.middleware.api.script.ApiConfig.SCRIPTS_GET_ALL;
import static com.pbs.middleware.api.script.ApiConfig.SCRIPTS_GET_BY_NAME;
import static com.pbs.middleware.api.script.ApiConfig.SCRIPTS_GET_FULL;
import static com.pbs.middleware.api.script.ApiConfig.SCRIPTS_GET_TEMPLATE;
import static com.pbs.middleware.api.script.ApiConfig.SCRIPTS_UPDATE;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
//@Validated
@RestController
@RequestMapping(SCRIPTS)
@RequiredArgsConstructor
@Api(tags = TagsConfig.SCRIPTS_TAG)
public class ScriptController {

    @NonNull
    private final ScriptService service;

    @NonNull
    private final ScriptMapper mapper;

    @PreAuthorize(SecurityConfig.ALLOW_ALL)
    @ApiOperation(value = "Retrieves all Scripts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List will all scripts"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = SCRIPTS_GET_ALL, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Script>> getAll() {
        return ok(service.getAll().stream().map(mapper::mapToDto).collect(Collectors.toList()));
    }

    @ApiOperation(value = "Retrieves one script")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Script object"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize(SecurityConfig.ALLOW_ALL)
    @GetMapping(value = SCRIPTS_GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Script> get(@PathVariable UUID id) {
        return ok(mapper.mapToDto(service.get(id)));
    }

    @ApiOperation(value = "Retrieves one script")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Script object"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize(SecurityConfig.ALLOW_ALL)
    @GetMapping(value = SCRIPTS_GET_BY_NAME, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Script> getByName(@PathVariable String name) {
        return ok(mapper.mapToDto(service.get(name)));
    }

    @ApiOperation(value = "Create new script")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "New script created!"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 409, message = "Script already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize(SecurityConfig.ALLOW_ALL)
    @PostMapping(value = SCRIPTS_CREATE, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Script> create(@Valid @RequestBody CreateScript scriptDto) {
        requireNonNull(scriptDto);
        return ok(mapper.mapToDto(service.create(mapper.mapFromDto(scriptDto))));
    }

    @ApiOperation(value = "Update script")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Script updated!"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Script not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize(SecurityConfig.ALLOW_ALL)
    @PutMapping(value = SCRIPTS_UPDATE, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Script> update(@PathVariable UUID id, @Valid @RequestBody UpdateScript scriptUpdateDto) {
        requireNonNull(scriptUpdateDto);
        return ok(mapper.mapToDto(service.update(id, mapper.mapFromDto(id, scriptUpdateDto))));
    }

    @PreAuthorize(SecurityConfig.ALLOW_ALL)
    @ApiOperation(value = "Remove specific script")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Script removed"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(
            @ApiParam(value = "Script unique ID", required = true)
            @PathVariable("id") UUID id) {
        service.delete(id);
        return ok().build();
    }

    @ApiOperation(value = "Retrieves configured script template")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Script template content"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize(SecurityConfig.ALLOW_ALL)
    @GetMapping(value = SCRIPTS_GET_TEMPLATE, produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getScriptTemplate() throws IOException {
        return ok(service.loadTemplate());
    }

    @ApiOperation(value = "Retrieves full(merged with template) script")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Full script"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize(SecurityConfig.ALLOW_ALL)
    @GetMapping(value = SCRIPTS_GET_FULL, produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getFullScript(@PathVariable UUID id) throws IOException {
        return ok(service.getFullScript(id));
    }

}
