package com.pbs.middleware.server.features.contact.rest;

import com.pbs.middleware.api.contact.ApiConfig;
import com.pbs.middleware.api.contact.CreateContact;
import com.pbs.middleware.api.contact.Contact;
import com.pbs.middleware.api.contact.UpdateContact;
import com.pbs.middleware.server.features.contact.service.ContactMapper;
import com.pbs.middleware.server.features.contact.service.ContactService;
import com.pbs.middleware.server.features.documentation.TagsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import static java.util.Objects.requireNonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ApiConfig.CONTACTS)
@RequiredArgsConstructor
@Api(tags = TagsConfig.CONTACTS_TAG)
public class ContactController {

    private static final Logger log = LoggerFactory.getLogger(ContactController.class);

    @NonNull
    private final ContactService contactService;

    @NonNull
    private final ContactMapper mapper;

    @PreAuthorize(ALLOW_ADMIN)
    @ApiOperation(value = "Retrieves all Contacts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List will all contacts"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = ApiConfig.CONTACTS_GET_ALL, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Contact>> getAll() {
        return ok(contactService.getAll().stream().map(mapper::mapToDto).collect(Collectors.toSet()));
    }

    @ApiOperation(value = "Retrieves one contact")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Contact object"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize(ALLOW_ADMIN)
    @GetMapping(value = ApiConfig.CONTACTS_GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Contact> get(@PathVariable UUID id) {
        return ok(mapper.mapToDto(contactService.get(id)));
    }

    @ApiOperation(value = "Retrieves all contact for type")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Contact object"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize(ALLOW_ADMIN)
    @GetMapping(value = ApiConfig.CONTACTS_GET_BY_TYPE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Contact>> getAllForType(
            @ApiParam(name = "type", value = "Type of contact we want to fetch", allowableValues = "JOB, UPLOAD, DOWNLOAD")
            @PathVariable(name = "type") String type) {
        return ok(contactService.findDtoByType(type).stream().map(mapper::mapToDto).collect(Collectors.toSet()));
    }

    @ApiOperation(value = "Create new contact")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "New contact created!"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 409, message = "Contact already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize(ALLOW_ADMIN)
    @PostMapping(value = ApiConfig.CONTACTS_CREATE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Contact> create(@RequestBody CreateContact contactDto) {
        requireNonNull(contactDto);
        return ok(mapper.mapToDto(contactService.create(mapper.mapFromDto(contactDto))));
    }

    @ApiOperation(value = "Update contact")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Contact updated!"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Contact not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize(ALLOW_ADMIN)
    @PutMapping(value = ApiConfig.CONTACTS_UPDATE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Contact> update(@PathVariable UUID id, @RequestBody UpdateContact contactUpdateDto) {
        requireNonNull(contactUpdateDto);
        return ok(mapper.mapToDto(contactService.update(id, mapper.mapFromDto(id, contactUpdateDto))));
    }

    @ApiOperation(value = "Delete contact")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Contact removed!"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize(ALLOW_ADMIN)
    @DeleteMapping(value = ApiConfig.CONTACTS_DELETE)
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        contactService.delete(id);
        return ok().build();
    }


}
