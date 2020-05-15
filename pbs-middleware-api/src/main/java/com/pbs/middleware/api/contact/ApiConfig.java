package com.pbs.middleware.api.contact;

public class ApiConfig {

    public static final String PREFIX = "/api";

    public static final String CONTACTS = PREFIX + "/contacts";

    public static final String CONTACTS_CREATE = "";
    public static final String CONTACTS_UPDATE = "/{id}";
    public static final String CONTACTS_DELETE = "/{id}";
    public static final String CONTACTS_GET = "/{id}";
    public static final String CONTACTS_GET_BY_TYPE = "/type/{type}";
    public static final String CONTACTS_GET_ALL = "";

    private ApiConfig() { }

}
