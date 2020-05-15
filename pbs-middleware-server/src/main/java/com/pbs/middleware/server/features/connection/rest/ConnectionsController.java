package com.pbs.middleware.server.features.connection.rest;

import com.pbs.middleware.api.connection.CreateConnection;
import com.pbs.middleware.api.connection.Connection;
import com.pbs.middleware.api.connection.UpdateConnection;
import com.pbs.middleware.server.features.connection.service.ConnectionMapper;
import com.pbs.middleware.server.features.connection.service.ConnectionService;
import com.pbs.middleware.server.features.documentation.Basic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
import static com.pbs.middleware.api.connection.ApiConfig.CONNECTIONS;
import static com.pbs.middleware.api.connection.ApiConfig.CONNECTIONS_CREATE;
import static com.pbs.middleware.api.connection.ApiConfig.CONNECTIONS_GET;
import static com.pbs.middleware.api.connection.ApiConfig.CONNECTIONS_GET_ALL;
import static com.pbs.middleware.api.connection.ApiConfig.CONNECTIONS_UPDATE;
import static com.pbs.middleware.server.features.documentation.TagsConfig.CONNECTIONS_TAG;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@Basic
@RestController
@RequestMapping(value = CONNECTIONS, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Api(
        tags = CONNECTIONS_TAG,
        produces = APPLICATION_JSON_VALUE,
        authorizations = {@Authorization(value = "JWT")})
public class ConnectionsController {

    @NonNull
    private final ConnectionService service;

    @NonNull
    private final ConnectionMapper mapper;

    @PreAuthorize(ALLOW_ADMIN)
    @ApiOperation(value = "Retrieves all Connections")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all connections"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = CONNECTIONS_GET_ALL, produces = APPLICATION_JSON_VALUE)
    public List<Connection> getAll() {
        return service.getAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @PreAuthorize(ALLOW_ADMIN)
    @ApiOperation(value = "Retrieves one connection")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Connection object"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = CONNECTIONS_GET, produces = APPLICATION_JSON_VALUE)
    public Connection get(@NonNull @PathVariable UUID id) {
        return mapper.toDto(service.get(id));
    }

    @PreAuthorize(ALLOW_ADMIN)
    @ApiOperation(value = "Create new connection")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "New connection created!"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 409, message = "Connection already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = CONNECTIONS_CREATE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Connection create(@Valid @RequestBody CreateConnection dto) {
        requireNonNull(dto);
        return mapper.toDto(service.create(mapper.fromDto(dto)));
    }

    @PreAuthorize(ALLOW_ADMIN)
    @ApiOperation(value = "Update connection")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Connection updated!"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Connection not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping(value = CONNECTIONS_UPDATE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Connection update(@PathVariable UUID id, @Valid @RequestBody UpdateConnection dto) {
        requireNonNull(dto);
        return mapper.toDto(service.update(id, mapper.fromDto(id, dto)));
    }

    @PreAuthorize(ALLOW_ADMIN)
    @ApiOperation(value = "Remove specific connection")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Connection removed"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(
            @ApiParam(value = "Connection unique ID", required = true)
            @PathVariable("id") UUID id) {
        service.delete(id);
        return ok().build();
    }

}
