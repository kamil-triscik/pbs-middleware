package com.pbs.middleware.api.connection;

public class ApiConfig {

    public static final String PREFIX = "/api";

    public static final String CONNECTIONS = PREFIX + "/connections";

    public static final String CONNECTIONS_CREATE = "";
    public static final String CONNECTIONS_UPDATE = "/{id}";
    public static final String CONNECTIONS_GET = "/{id}";

    public static final String CONNECTIONS_GET_ALL = "";

    private ApiConfig() { }
}
