package com.pbs.middleware.server.features.users.service.password;

import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {

    private static final String DEFAULT_PASS = "1234";

    public String generate() {
        return DEFAULT_PASS;
    }
}
