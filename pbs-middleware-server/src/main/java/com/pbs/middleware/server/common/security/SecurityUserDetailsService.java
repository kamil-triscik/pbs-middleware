package com.pbs.middleware.server.common.security;

import com.pbs.middleware.server.features.users.repository.User;
import com.pbs.middleware.server.features.users.repository.UserRepository;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(SecurityUserDetailsService.class);

    @NonNull
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        UserDetails userDetails = userRepository.findByEmail(email)
                .map(SecurityUserMapper::mapUserToUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        log.info("Loaded {} with {}", userDetails.getUsername(), userDetails.getAuthorities());
        return userDetails;
    }

    public Optional<User> getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof SecurityUser) {
            return Optional.of(SecurityUserMapper.mapToUser((SecurityUser) principal));
        }

        if (principal instanceof UserDetails) {
            return Optional.of(SecurityUserMapper.mapToUser((UserDetails) principal));
        }

        throw new AuthenticationException("Principal cannot be mapped to UserDetails") {
        };
    }

    public User getAuthenticatedUser() {
        return getLoggedUser()
                .orElseThrow(() -> new AuthenticationException("Not authorized") {
                });
    }
}
