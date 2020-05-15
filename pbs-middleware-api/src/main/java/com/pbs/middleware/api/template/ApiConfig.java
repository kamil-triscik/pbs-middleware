package com.pbs.middleware.api.template;

public class ApiConfig {

    public static final String PREFIX = "/api";

    public static final String TEMPLATES = PREFIX + "/templates";

    public static final String TEMPLATES_CREATE = "";
    public static final String TEMPLATES_UPDATE = "/{templateId}";
    public static final String TEMPLATES_GET = "/{templateId}";
    public static final String TEMPLATES_DELETE = "/{templateId}";
    public static final String TEMPLATES_COPY = "/{templateId}/{newId}";

    public static final String TEMPLATES_GET_ALL = "";

    public static final String TEMPLATES_EXISTS = "/{templateId}/exists";

    public static final String TEMPLATES_QSUB_ID = "/qsub/{templateId}";
    public static final String TEMPLATES_QSUB = "/qsub";

    private ApiConfig() { }
}
