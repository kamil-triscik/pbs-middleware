package com.pbs.middleware.api.script;

public class ApiConfig {

    public static final String PREFIX = "/api";

    public static final String SCRIPTS = PREFIX + "/scripts";

    public static final String SCRIPTS_CREATE = "";
    public static final String SCRIPTS_UPDATE = "/{id}";
    public static final String SCRIPTS_GET = "/{id}";
    public static final String SCRIPTS_GET_BY_NAME = "/name/{name}";
    public static final String SCRIPTS_GET_ALL = "";

    public static final String SCRIPTS_GET_TEMPLATE = "/template";
    public static final String SCRIPTS_GET_FULL = SCRIPTS_GET + "/full";

}
