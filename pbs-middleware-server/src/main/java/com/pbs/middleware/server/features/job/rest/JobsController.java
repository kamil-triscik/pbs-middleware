package com.pbs.middleware.server.features.job.rest;

import com.pbs.middleware.api.job.Job;
import com.pbs.middleware.api.job.JobRestartRequest;
import com.pbs.middleware.api.job.KillJobRequest;
import com.pbs.middleware.api.job.JobSubmit;
import com.pbs.middleware.server.features.job.domain.JobState;
import com.pbs.middleware.server.features.job.events.JobEvent;
import com.pbs.middleware.server.features.job.service.JobService;
import com.pbs.middleware.server.features.pbs.qstat.Qstat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.pbs.middleware.server.common.security.SecurityConfig.ALLOW_ADMIN;
import static com.pbs.middleware.server.common.security.SecurityConfig.ALLOW_ALL;
import static com.pbs.middleware.api.job.ApiConfig.JOB_PREFIX;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_GET_ALL;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_DELETE;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_GET;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_GET_EVENTS;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_GET_QSTAT;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_GROUP;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_KILL;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_RESTART;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_SUBMIT;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_SUBMIT_WITH_ID;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_TAG;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Api(tags = JOBS_TAG)
@RequestMapping(JOB_PREFIX)
@RequiredArgsConstructor
public class JobsController {

    @NonNull
    private final JobService service;

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(value = "Retrieve all jobs")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all jobs"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = JOBS_GET_ALL, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<UUID>> getJobs() {
        return ok(service.getAllJobs());
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(value = "Submit new job")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Job successfully submitted!"),
            @ApiResponse(code = 409, message = "Job already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = JOBS_SUBMIT_WITH_ID, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> submit(
            @ApiParam(value = "Job unique ID", required = true)
            @PathVariable("id") UUID id,
            @ApiParam(value = "Job parameters", required = true)
            @RequestBody JobSubmit submitRequest) {
        return accepted().body(service.submitJob(id, submitRequest));
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(value = "Submit new job")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Job successfully submitted!"),
            @ApiResponse(code = 409, message = "Job already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = JOBS_SUBMIT, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> submit(
            @ApiParam(value = "Job parameters", required = true)
            @RequestBody JobSubmit submitRequest) {
        return submit(UUID.randomUUID(), submitRequest);
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(value = "Fetch job state")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Job fetched!"),
            @ApiResponse(code = 404, message = "Job not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = JOBS_GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Job> get(
            @ApiParam(value = "Job unique ID", required = true)
            @PathVariable("id") UUID id) {
        return ok(service.get(id));
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(value = "Fetch job qstat")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Job qstat fetched!"),
            @ApiResponse(code = 404, message = "Job not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = JOBS_GET_QSTAT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Qstat> getQstat(
            @ApiParam(value = "Job unique ID", required = true)
            @PathVariable("id") UUID id) {
        return ok(service.getQstat(id));
    }

    @PreAuthorize(ALLOW_ADMIN)
    @ApiOperation(value = "Fetch job events")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Job events fetched!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = JOBS_GET_EVENTS, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JobEvent>> getEvents(
            @ApiParam(value = "Job unique ID", required = true)
            @PathVariable("id") UUID id) {
        return ok(service.getEvents(id));
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(value = "Fetch jobs group state")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Group jobs status fetched!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = JOBS_GROUP, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<JobState>> getGroupState(
            @ApiParam(value = "Group unique Name", required = true)
            @PathVariable("group") String group) {
        return ok(service.getGroupState(group));
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(value = "Remove all events for specific job")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Job events removed"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping(value = JOBS_DELETE)
    public ResponseEntity<?> delete(
            @ApiParam(value = "Job unique ID", required = true)
            @PathVariable("id") UUID id) {
        service.delete(id);
        return ok().build();
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(value = "Kill job")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Job killed"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = JOBS_KILL, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> kill(
            @ApiParam(value = "Job unique ID", required = true)
            @PathVariable("id") UUID id,
            @ApiParam(value = "Job failure parameters", required = true)
            @RequestBody KillJobRequest jobFailed) {
        service.failed(id, jobFailed);
        return ok().build();
    }

    @PreAuthorize(ALLOW_ALL)
    @ApiOperation(value = "Restart job")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Restart job"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = JOBS_RESTART, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> restart(
            @ApiParam(value = "Job unique ID", required = true)
            @PathVariable("id") UUID id,
            @ApiParam(value = "Job parameters", required = true)
            @RequestBody JobRestartRequest restartRequest) {
        service.restart(id, restartRequest);
        return ok().build();
    }
}
