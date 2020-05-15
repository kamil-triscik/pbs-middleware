package com.pbs.middleware.api.user;

public class ApiConfig {

    public static final String PREFIX = "/api";

    public static final String USERS = PREFIX + "/users";

    public static final String USERS_ME = "/me";
    public static final String USERS_LOGIN_FAILED = "/login-failed";

    public static final String USERS_CREATE = "/";
    public static final String USERS_UPDATE = "/{id}";
    public static final String USERS_GET = "/{id}";
    public static final String USERS_DISABLE = "/{id}/disable";
    public static final String USERS_ENABLE = "/{id}/enable";
    public static final String USERS_RESET_PW = "/{id}/reset-password";
    public static final String USERS_CHANGE_PW = "/{id}/change-password";

    public static final String USERS_GET_ALL = "";

    private ApiConfig() {
    }
}
