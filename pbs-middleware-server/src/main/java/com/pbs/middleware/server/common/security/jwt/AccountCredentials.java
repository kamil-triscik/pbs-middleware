package com.pbs.middleware.server.common.security.jwt;

import lombok.Data;

@Data
public class AccountCredentials {

    private String email = "admin@localhost";

    private String password = "1234";

}
