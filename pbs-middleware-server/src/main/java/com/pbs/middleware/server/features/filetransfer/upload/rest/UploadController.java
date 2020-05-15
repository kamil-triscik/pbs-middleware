package com.pbs.middleware.server.features.filetransfer.upload.rest;

import com.pbs.middleware.api.upload.Upload;
import com.pbs.middleware.api.upload.UploadRequest;
import com.pbs.middleware.server.features.filetransfer.upload.exceptions.ReceivedFileReadingException;
import com.pbs.middleware.server.features.filetransfer.upload.service.UploadMapper;
import com.pbs.middleware.server.features.filetransfer.upload.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_PREFIX;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_CANCEL;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_DELETE;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_FILE;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_FILE_MULTIPLE;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_GET;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_GET_ALL;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_GET_EVENTS;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_START;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_START_WITH_ID;
import static com.pbs.middleware.server.common.security.SecurityConfig.ALLOW_ADMIN;
import static com.pbs.middleware.server.common.security.SecurityConfig.ALLOW_ALL;
import static com.pbs.middleware.server.features.documentation.TagsConfig.UPLOADS_TAG;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Api(tags = UPLOADS_TAG)
@RequestMapping(UPLOADS_PREFIX)
@RequiredArgsConstructor
public class UploadController {

    @NonNull
    private final UploadService service;
    @NonNull
    private final UploadMapper mapper;

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(value = "Retrieve all uploads ids")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all uploads"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = UPLOADS_GET_ALL, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<UUID>> getUploads() {
        return ok(service.getUploads());
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(
            value = "Data upload request",
            consumes = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Upload request accepted!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = UPLOADS_START, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> startUpload(
            @ApiParam(name = "request", value = "Upload request", required = true)
            @RequestBody UploadRequest dto) {
        return startUpload(UUID.randomUUID(), dto);
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(
            value = "Data upload request",
            consumes = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Upload request accepted!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = UPLOADS_START_WITH_ID, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> startUpload(
            @ApiParam(value = "Upload unique ID", required = true)
            @PathVariable("id") UUID id,
            @ApiParam(name = "connection", value = "Upload connection", required = true)
            @RequestBody UploadRequest dto) {
        service.upload(id, dto.getConnection(), dto.getDestination(), dto.getEmail(), dto.getNotify(), dto.getDescription(), dto.getFiles());
        return accepted().body(id);
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(
            value = "Data upload request",
            consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Upload accepted!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = UPLOADS_FILE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(
            @ApiParam(value = "Upload unique ID", required = true)
            @PathVariable("id") UUID id,
            @ApiParam(name = "file", value = "File to upload", required = true)
            @RequestPart("file") MultipartFile file) {
        try {
            service.uploadFile(id, file.getBytes(), file.getOriginalFilename());
            return accepted().build();
        } catch (IOException e) {
            throw new ReceivedFileReadingException(file, e);
        }
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(
            value = "Data upload request",
            consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Upload accepted!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = UPLOADS_FILE_MULTIPLE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMultipleFile(
            @ApiParam(value = "Upload unique ID", required = true)
            @PathVariable("id") UUID id,
            @ApiParam(name = "files", value = "Files to upload", required = true)
            @RequestPart("files") MultipartFile[] files) {
        Stream.of(files).forEach(file -> uploadFile(id, file));
        return accepted().build();
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(value = "Fetch upload")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Upload successfully loaded!"),
            @ApiResponse(code = 404, message = "Upload not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = UPLOADS_GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Upload> get(
            @ApiParam(value = "Upload unique ID", required = true)
            @PathVariable("id") UUID id) {
        return ok(mapper.toDto(service.get(id)));
    }

    @PreAuthorize(ALLOW_ADMIN)
    @ApiOperation(value = "Fetch upload events")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Upload events fetched!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = UPLOADS_GET_EVENTS, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getEvents(
            @ApiParam(value = "Upload unique ID", required = true)
            @PathVariable("id") UUID id) {
        return ok(service.getEvents(id));
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(
            value = "Cancel upload process.",
            notes = "<b>User can cancel uploading process.<br><br>" +
                    "Enabled only in case upload process is in one of states:" +
                    "<ul><li>REQUESTED</li><li>IN_PROGRESS</li></ul></b>")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Cancel request accepted!"),
            @ApiResponse(code = 403, message = "Cancel operation forbidden!"),
            @ApiResponse(code = 404, message = "Upload with provided ID not found!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = UPLOADS_CANCEL)
    public ResponseEntity<?> cancel(
            @ApiParam(value = "Upload unique ID", required = true)
            @PathVariable("id") UUID id,
            @ApiParam(name = "reason", value = "Cancel reason(optional)")
            @RequestParam(name = "reason", required = false) String reason,
            @ApiParam(name = "removeUploaded", value = "Flag if already uploaded files should be removed")
            @RequestParam(name = "removeUploaded", required = false) Boolean removeUploaded) {
        MDC.put("objectId", "upload-" + id.toString());
        service.cancel(id, reason, removeUploaded);
        return accepted().build();
    }


    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(value = "Remove all events for specific upload")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Upload events removed"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping(value = UPLOADS_DELETE)
    public ResponseEntity<?> delete(
            @ApiParam(value = "Upload unique ID", required = true)
            @PathVariable("id") UUID id) {
        service.delete(id);
        return ok().build();
    }
}
