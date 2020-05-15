package com.pbs.middleware.server.common.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * If application detects that there are no users with ADMIN role
 * then a default admin user is created with the following properties.
 *
 * @see BuiltInAdminService
 */
@ConfigurationProperties(prefix = "middleware.server.security.default.admin")
@Getter
@Setter
public class DefaultAdminProperties {

    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;

}
