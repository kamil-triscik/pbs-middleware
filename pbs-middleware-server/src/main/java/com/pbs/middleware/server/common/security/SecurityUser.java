package com.pbs.middleware.server.common.security;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@EqualsAndHashCode
public class SecurityUser extends User {

    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final String email;

    SecurityUser(UUID id, String firstName, String lastName, String email, UserDetails userDetails) {
        super(userDetails.getUsername(), userDetails.getPassword(), userDetails.isEnabled(), userDetails.isAccountNonExpired(),
                userDetails.isCredentialsNonExpired(), userDetails.isAccountNonLocked(), userDetails.getAuthorities());
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
