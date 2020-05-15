package com.pbs.middleware.server.common.security;

import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUserBuilder {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private UserDetails userDetails;

    public SecurityUserBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public SecurityUserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public SecurityUserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public SecurityUserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public SecurityUserBuilder userDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
        return this;
    }

    public SecurityUser build() {
        return new SecurityUser(id, firstName, lastName, email, userDetails);
    }
}