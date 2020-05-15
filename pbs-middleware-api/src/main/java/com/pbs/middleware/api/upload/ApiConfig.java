package com.pbs.middleware.api.upload;

import java.util.List;
import java.util.stream.Collectors;

public class ApiConfig {

    public static final String PREFIX = "/api";

    public static final String UPLOADS_PREFIX = PREFIX + "/uploads";

    public static final String UPLOADS_GET = "/{id}";

    public static final String UPLOADS_START = "/start";
    public static final String UPLOADS_START_WITH_ID = "/start/{id}";

    public static final String UPLOADS_FILE = "/{id}/file";
    public static final String UPLOADS_FILE_MULTIPLE = "/{id}/multiple";

    public static final String UPLOADS_CANCEL = "/{id}/cancel";

    public static final String UPLOADS_GET_ALL = "";

    public static final String UPLOADS_DELETE = "/{id}";

    public static final String UPLOADS_GET_EVENTS = "/{id}/events";

    public static List<String> forOwnerShip() {
        return List.of(UPLOADS_GET, UPLOADS_FILE, UPLOADS_FILE_MULTIPLE, UPLOADS_CANCEL, UPLOADS_DELETE)
                .stream()
                .map(it -> it.replace("{id}", "*"))
                .map(it -> UPLOADS_PREFIX + it)
                .collect(Collectors.toList());
    }

    private ApiConfig() {
    }

}
