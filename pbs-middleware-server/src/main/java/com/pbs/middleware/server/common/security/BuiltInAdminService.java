package com.pbs.middleware.server.common.security;

import com.pbs.middleware.server.features.users.repository.User;
import com.pbs.middleware.server.features.users.repository.UserRepository;
import com.pbs.middleware.server.features.users.repository.UserRole;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The {@code BuiltInAdminService} detects that there are no users with ADMIN role
 * then creates a default admin user with the configured security properties.
 *
 * @see SecurityProperties
 */
@Service
@RequiredArgsConstructor
public class BuiltInAdminService {

    private static final Logger log = LoggerFactory.getLogger(BuiltInAdminService.class);

    @NonNull
    private final PasswordEncoder passwordEncoder;
    @NonNull
    private final DefaultAdminProperties defaultAdminProperties;
    @NonNull
    private final UserRepository userRepository;

    @EventListener({ApplicationStartedEvent.class})
    public void createAdminIfMissing(ApplicationStartedEvent event) {
        log.info("Checking for users with {} ...", UserRole.ROLE_ADMIN);
        if (!hasAdmins()) {
            User defaultAdmin = getDefaultAdmin();
            log.warn("Creating default administrator user {} with password {} ...",
                    defaultAdmin.getUsername(), defaultAdmin.getPassword());
            defaultAdmin.setPassword(passwordEncoder.encode(defaultAdmin.getPassword()));
            create(defaultAdmin);
        }
    }

    private boolean hasAdmins() {
        return userRepository.existsByRole(UserRole.ROLE_ADMIN);
    }

    private User getDefaultAdmin() {
        User user = new User();

        user.setUsername(defaultAdminProperties.getUsername());
        user.setFirstName(defaultAdminProperties.getFirstname());
        user.setLastName(defaultAdminProperties.getLastname());
        user.setPassword(defaultAdminProperties.getPassword());
        user.setEmail(defaultAdminProperties.getEmail());
        user.setRole(UserRole.ROLE_ADMIN);

        return user;
    }

    private void create(User defaultAdmin) {
        userRepository.save(defaultAdmin);
    }

}