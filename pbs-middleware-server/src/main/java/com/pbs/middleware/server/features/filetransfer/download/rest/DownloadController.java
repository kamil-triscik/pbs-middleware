package com.pbs.middleware.server.features.filetransfer.download.rest;

import com.pbs.middleware.api.download.ApiConfig;
import com.pbs.middleware.api.download.Download;
import com.pbs.middleware.api.download.DownloadRequest;
import com.pbs.middleware.server.features.filetransfer.download.service.DownloadFile;
import com.pbs.middleware.server.features.filetransfer.download.service.DownloadMapper;
import com.pbs.middleware.server.features.filetransfer.download.service.DownloadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_PREFIX;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_DELETE;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_EVENTS;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_GET;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_GET_ALL_FILES;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_GET_FILE;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_START;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_START_WITH_ID;
import static com.pbs.middleware.server.common.security.SecurityConfig.ALLOW_ADMIN;
import static com.pbs.middleware.server.common.security.SecurityConfig.ALLOW_ALL;
import static com.pbs.middleware.server.features.documentation.TagsConfig.DOWNLOADS_TAG;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.ok;

//@Validated
@RestController
@Api(tags = DOWNLOADS_TAG)
@RequestMapping(DOWNLOADS_PREFIX)
@RequiredArgsConstructor
public class DownloadController {

    @NonNull
    private final DownloadService service;
    @NonNull
    private final DownloadMapper mapper;

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(
            value = "Retrieve all downloads ids",
            produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all downloads"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<UUID>> getDownloads() {
        return ok(service.getDownloads());
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(
            value = "Download files request",
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Download requested accepted!"),
            @ApiResponse(code = 400, message = "Invalid download request"),
            @ApiResponse(code = 403, message = "Download process already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(
            value = DOWNLOADS_START,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> create(
            @ApiParam(name = "Request", value = "Download request", required = true)
            @RequestBody DownloadRequest dto) {
        return create(UUID.randomUUID(), dto);
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(
            value = "Download files request",
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Download requested accepted!"),
            @ApiResponse(code = 400, message = "Invalid download request"),
            @ApiResponse(code = 403, message = "Download process already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(
            value = DOWNLOADS_START_WITH_ID,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> create(
            @ApiParam(value = "Download unique ID", required = true)
            @PathVariable("id") UUID id,
            @ApiParam(name = "Request", value = "Download request", required = true)
            @RequestBody @Valid DownloadRequest dto) {
        MDC.put("objectId", "download-" + id.toString());
        service.download(
                id,
                dto.getConnection(),
                dto.getEmail(),
                dto.getNotify(),
                dto.getFolder(),
                dto.getDescription(),
                dto.getFiles(),
                dto.getRemove());
        return accepted().body(id);
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(value = "Get download process status and detail info!")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Download process data successfully loaded!"),
            @ApiResponse(code = 404, message = "Download not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(
            value = DOWNLOADS_GET,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Download> get(
            @ApiParam(value = "Download unique ID", required = true)
            @PathVariable("id") UUID id) {
        return ok(mapper.toDto(service.get(id)));
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(
            value = "Download file",
            produces = APPLICATION_OCTET_STREAM_VALUE + "," + APPLICATION_JSON_VALUE,
            notes = "<b>User can download fetched file.<br>" +
                    "Enabled only in case download process is in state FETCHED.</b>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Download successfully loaded!"),
            @ApiResponse(code = 403, message = "File not prepared!"),
            @ApiResponse(code = 404, message = "Download process id or file not found!"),
            @ApiResponse(code = 500, message = "Internal server error!")
    })
    @GetMapping(
            value = DOWNLOADS_GET_FILE,
            produces = APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadFile(
            @ApiParam(value = "Download unique ID", required = true)
            @PathVariable("id") UUID id,
            @ApiParam(value = "Download filename", required = true)
            @PathVariable("filename") String filename) {

        DownloadFile file = service.getFile(id, filename);

        return ok()
                .contentLength(file.getLength())
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(APPLICATION_OCTET_STREAM)
                .body(file.getResource());
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(
            value = "Download all files.",
            produces = APPLICATION_OCTET_STREAM_VALUE + "," + APPLICATION_JSON_VALUE,
            notes = "<b>User can download all fetched files as one as zip file.<br>" +
                    "Enabled only in case download process is in state FETCHED.</b>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Download successfully loaded!"),
            @ApiResponse(code = 403, message = "Files not prepared!"),
            @ApiResponse(code = 404, message = "Download or file not found!"),
            @ApiResponse(code = 500, message = "Internal server error!")
    })
    @GetMapping(
            value = DOWNLOADS_GET_ALL_FILES,
            produces = {APPLICATION_OCTET_STREAM_VALUE, APPLICATION_JSON_VALUE})
    public ResponseEntity<Resource> downloadAllFiles(
            @ApiParam(value = "Download unique ID", required = true)
            @PathVariable("id") UUID id) {

        DownloadFile file = service.getFiles(id);

        return ok()
                .contentLength(file.getLength())
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(APPLICATION_OCTET_STREAM)
                .body(file.getResource());
    }

    @PreAuthorize(ALLOW_ADMIN)
    @ApiOperation(value = "Fetch all download events")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Download events fetched!"),
            @ApiResponse(code = 404, message = "Download with provided ID not found!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(
            value = DOWNLOADS_EVENTS,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getEvents(
            @ApiParam(value = "Download unique ID", required = true)
            @PathVariable("id") UUID id) {
        return ok(service.getEvents(id));
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(
            value = "Confirm files download.",
            notes = "<b>User must confirm that he download all fetched files and download process can be finished.<br>" +
                    "Enabled only in case download process is in state FETCHED.</b>")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Cancel request accepted!"),
            @ApiResponse(code = 403, message = "Cancel operation forbidden!"),
            @ApiResponse(code = 404, message = "Download with provided ID not found!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(ApiConfig.DOWNLOADS_CONFIRM)
    public ResponseEntity<?> confirm(
            @ApiParam(value = "Download unique ID", required = true)
            @PathVariable("id") UUID id) {
        MDC.put("objectId", "download-" + id.toString());
        service.confirm(id);
        return accepted().build();
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(
            value = "Cancel download process.",
            notes = "<b>User can cancel downloading process.<br><br>" +
                    "Enabled only in case download process is in one of states:" +
                    "<ul><li>REQUESTED</li><li>IN_PROGRESS</li><li>FETCHED</li></ul></b>")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Cancel request accepted!"),
            @ApiResponse(code = 403, message = "Cancel operation forbidden!"),
            @ApiResponse(code = 404, message = "Download with provided ID not found!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(ApiConfig.DOWNLOADS_CANCEL)
    public ResponseEntity<?> cancel(
            @ApiParam(value = "Download unique ID", required = true)
            @PathVariable("id") UUID id,
            @ApiParam(name = "reason", value = "Cancel reason(optional)")
            @RequestParam(name = "reason", required = false) String reason) {
        MDC.put("objectId", "download-" + id.toString());
        service.cancel(id, reason);
        return accepted().build();
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(value = "Remove all events for specific download")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Download events removed"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping(value = DOWNLOADS_DELETE)
    public ResponseEntity<?> delete(
            @ApiParam(value = "Download unique ID", required = true)
            @PathVariable("id") UUID id) {
        service.delete(id);
        return ok().build();
    }
}
