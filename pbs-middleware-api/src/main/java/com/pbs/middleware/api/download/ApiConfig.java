package com.pbs.middleware.api.download;

import java.util.List;
import java.util.stream.Collectors;

public class ApiConfig {

    public static final String PREFIX = "/api";

    public static final String DOWNLOADS_PREFIX = PREFIX + "/downloads";

    public static final String DOWNLOADS_GET = "/{id}";

    public static final String DOWNLOADS_START = "/start";
    public static final String DOWNLOADS_START_WITH_ID = "/{id}/start";

    public static final String DOWNLOADS_GET_FILE = "/{id}/file/{filename}";
    public static final String DOWNLOADS_GET_ALL_FILES = "/{id}/files";
    public static final String DOWNLOADS_CONFIRM = "/{id}/confirm";
    public static final String DOWNLOADS_CANCEL = "/{id}/cancel";
    public static final String DOWNLOADS_DELETE = "/{id}";

    public static final String DOWNLOADS_EVENTS = "/{id}/events";

    public static List<String> forOwnerShip() {
        return List.of(DOWNLOADS_GET, DOWNLOADS_GET_FILE, DOWNLOADS_GET_ALL_FILES, DOWNLOADS_CONFIRM, DOWNLOADS_CANCEL, DOWNLOADS_DELETE)
                .stream()
                .map(it -> it.replace("{id}", "*"))
                .map(it -> DOWNLOADS_PREFIX + it)
                .collect(Collectors.toList());
    }

    private ApiConfig() { }
}
