package com.pbs.middleware.server.common.security;

import com.pbs.middleware.server.common.security.jwt.TokenClaims;
import com.pbs.middleware.server.features.users.repository.User;
import com.pbs.middleware.server.features.users.repository.UserRole;
import io.jsonwebtoken.Claims;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.userdetails.UserDetails;

import static java.util.Collections.singletonList;
import static org.springframework.security.core.userdetails.User.builder;

@UtilityClass
public class SecurityUserMapper {

    static UserDetails mapUserToUserDetails(User user) {
        return new SecurityUserBuilder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .userDetails(builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .disabled(user.isDisabled())
                        .authorities(singletonList(user.getRole()))
                        .accountExpired(false)
                        .credentialsExpired(false)
                        .accountLocked(false)
                        .build())
                .build();
    }

    static User mapToUser(SecurityUser securityUser) {
        User user = mapToUser((UserDetails) securityUser);
        user.setId(securityUser.getId());
        user.setFirstName(securityUser.getFirstName());
        user.setLastName(securityUser.getLastName());

        return user;
    }

    static User mapToUser(UserDetails userDetails) {
        User user = new User();
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        UserRole role = (UserRole) userDetails.getAuthorities().stream().findFirst().orElseThrow();
        user.setRole(role);

        return user;
    }

    public static SecurityUser mapClaimsToSecurityUser(Claims claims) {
        return new SecurityUserBuilder()
                .id(UUID.fromString ((String) claims.get(TokenClaims.USER_ID)))
                .firstName((String) claims.get(TokenClaims.FIRST_NAME))
                .lastName((String) claims.get(TokenClaims.LAST_NAME))
                .email((String) claims.get(TokenClaims.EMAIL))
                .userDetails(builder()
                        .username(claims.getSubject())
                        .password("")
                        .authorities(((ArrayList<String>) claims.get(TokenClaims.ROLES))
                                .stream().map(UserRole::valueOf)
                                .collect(Collectors.toList()))
                        .accountExpired(false)
                        .credentialsExpired(false)
                        .accountLocked(false)
                        .build())
                .build();
    }
}
