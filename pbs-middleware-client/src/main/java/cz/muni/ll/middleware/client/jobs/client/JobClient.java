package cz.muni.ll.middleware.client.jobs.client;

import com.pbs.middleware.api.job.Job;
import com.pbs.middleware.api.job.JobRestartRequest;
import com.pbs.middleware.api.job.JobSubmit;
import com.pbs.middleware.api.job.KillJobRequest;
import cz.muni.ll.middleware.client.rest.exceptions.LLMClientException;
import cz.muni.ll.middleware.client.jobs.domain.job.JobInfo;
import com.pbs.middleware.api.job.JobQstat;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.util.Set;
import java.util.UUID;

import static com.pbs.middleware.api.job.ApiConfig.JOBS_DELETE;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_KILL;
import static com.pbs.middleware.api.job.ApiConfig.JOB_PREFIX;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_GET;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_GET_QSTAT;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_GROUP;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_RESTART;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_SUBMIT;
import static com.pbs.middleware.api.job.ApiConfig.JOBS_SUBMIT_WITH_ID;

public interface JobClient {

    @RequestLine("GET " + JOB_PREFIX)
    @Headers("Accept: application/json")
    Set<UUID> getAllJobs() throws LLMClientException;

    @RequestLine("POST " + JOB_PREFIX + JOBS_SUBMIT)
    @Headers({"Content-type: application/json"})
    UUID submitJob(JobSubmit submitRequest);

    @RequestLine("POST " + JOB_PREFIX + JOBS_SUBMIT_WITH_ID)
    @Headers({"Content-type: application/json"})
    UUID submitJob(@Param("id") UUID id, JobSubmit submitRequest);

    @RequestLine("GET " + JOB_PREFIX + JOBS_GET)
    @Headers("Accept: application/json")
    Job getJob(@Param("id") UUID id);

    @RequestLine("GET " + JOB_PREFIX + JOBS_GET_QSTAT)
    @Headers("Accept: application/json")
    JobQstat getQstat(@Param("id") UUID id);

    @RequestLine("GET " + JOB_PREFIX + JOBS_GROUP)
    @Headers("Accept: application/json")
    Set<JobInfo> getJobsGroupState(@Param("group") String groupName);

    @RequestLine("POST " + JOB_PREFIX + JOBS_RESTART)
    @Headers({"Content-type: application/json"})
    void restart(@Param("id") UUID id, JobRestartRequest restartRequest);


    @RequestLine("POST " + JOB_PREFIX + JOBS_KILL)
    @Headers({"Content-type: application/json"})
    void kill(@Param("id") UUID id, KillJobRequest killJobRequest);

    @RequestLine("DELETE " + JOB_PREFIX + JOBS_DELETE)
    void delete(@Param("id") UUID id);

}
