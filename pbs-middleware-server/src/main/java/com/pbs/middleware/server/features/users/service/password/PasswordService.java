package com.pbs.middleware.server.features.users.service.password;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The {@code PasswordService} provides method for handling password such as generating or encoding.
 *
 */
@Service
@RequiredArgsConstructor
public class PasswordService {

    @NonNull
    private final PasswordEncoder passwordEncoder;
    @NonNull
    private final PasswordGenerator passwordGenerator;

    /**
     * Generates a password using configured {@link PasswordGenerator}.
     *
     * @return a generated password
     */
    public String generatePassword() {
        return passwordGenerator.generate();
    }

    /**
     * Encodes the {@code rawPassword} using configured {@link org.springframework.security.crypto.password.PasswordEncoder}.
     *
     * @param rawPassword raw (unencoded) password
     * @return encoded password
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Compares a {@code rawPassword} with {@code encodedPassword} and returns {@code true} if passwords match.
     *
     * @param rawPassword raw password - usually comes from change password request
     * @param encodedPassword encoded password - usually comes from loaded user
     * @return {@code true} if passwords match or {@code false} otherwise
     */
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
