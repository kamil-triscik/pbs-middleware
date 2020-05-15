package com.pbs.middleware.api.job;

import java.util.List;
import java.util.stream.Collectors;

public class ApiConfig {

    public static final String PREFIX = "/api";
    public static final String JOB_PREFIX = PREFIX + "/jobs";

    public static final String JOBS_TAG = "Jobs";

    public static final String JOBS_GET = "/{id}";
    public static final String JOBS_GET_ALL = "";

    public static final String JOBS_SUBMIT = "/submit";
    public static final String JOBS_SUBMIT_WITH_ID = "/submit/{id}";

    public static final String JOBS_GET_QSTAT = "/{id}/qstat";
    public static final String JOBS_GET_EVENTS = "/{id}/events";
    public static final String JOBS_DELETE = "/{id}";
    public static final String JOBS_GROUP = "/group/{group}";

    public static final String JOBS_KILL = "/{id}/kill";
    public static final String JOBS_RESTART = "/{id}/restart";

    public static List<String> forOwnerShip() {
        return List.of(JOBS_GET, JOBS_GET_QSTAT)
                .stream()
                .map(it -> it.replace("{id}", "*"))
                .map(it -> JOB_PREFIX + it)
                .collect(Collectors.toList());
    }

    private ApiConfig() { }

}
