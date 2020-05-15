package com.pbs.middleware.server.features.users.service;

import com.pbs.middleware.server.common.exception.ObjectConflictException;
import com.pbs.middleware.server.features.users.repository.User;
import com.pbs.middleware.server.features.users.repository.UserRepository;
import com.pbs.middleware.server.features.users.repository.UserRole;
import com.pbs.middleware.server.features.users.service.password.PasswordService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

//import com.pbs.middleware.features.notification.email.EmailService;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final PasswordService passwordService;

//    @NonNull
//    private final EmailService emailService;

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User get(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> UserNotFoundException.of(id));
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional(noRollbackFor = MailSendException.class)
    public User create(User user) {
        validateUniqueUsername(user);
        String password = passwordService.generatePassword();
        user.setPassword(passwordService.encodePassword(password));
        user = userRepository.save(user);
//        sendEmailNotification(user.getEmail(), password);
        return user;
    }

    @Transactional
    public User update(UUID id, User user) {
        User origUser = userRepository.findById(id).orElseThrow(() -> UserNotFoundException.of(id));
        origUser.setEmail(user.getEmail());
        origUser.setFirstName(user.getFirstName());
        origUser.setLastName(user.getLastName());
        origUser.setDisabled(user.isDisabled());
        origUser.setId(id);
        return userRepository.save(origUser);
    }

    @Transactional(noRollbackFor = MailSendException.class)
    public void resetPassword(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> UserNotFoundException.of(id));
        String password = passwordService.generatePassword();
        user.setPassword(passwordService.encodePassword(password));
        user = userRepository.save(user);
//        sendEmailNotification(user.getEmail(), password);
    }

    @Transactional
    public void enableDisableUser(UUID id, boolean disabled) {
        User user = userRepository.findById(id).orElseThrow(() -> UserNotFoundException.of(id));
        user.setDisabled(disabled);
        userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> UserNotFoundException.of(username));
    }

    @Transactional
    public void changePassword(UUID id, String oldRawPassword, String newRawPassword) {
        requireNonNull(id, "User ID must be provided.");
        requireNonNull(oldRawPassword, "Old password must be provided.");
        requireNonNull(newRawPassword, "New password must be provided.");
        User user = userRepository.findById(id).orElseThrow(() -> UserNotFoundException.of(id));
        if (passwordService.checkPassword(oldRawPassword, user.getPassword())) {
            user.setPassword(passwordService.encodePassword(newRawPassword));
            userRepository.save(user);
            log.info("Password changed successfully for user {}", user.getUsername());
        } else {
            log.error("Change password failed for user {}: password is invalid.", id);
            throw new BadCredentialsException("Password is invalid.");
        }
    }

//    private void sendEmailNotification(String email, String password) {
//        try {
//            emailService.sendMessage(email, "Password reset", Map.of("password", password));
//        } catch (IOException | TemplateException | MessagingException e) {
//            log.error("Email has not been sent", e);
//        }
//    }

    public List<User> getAdmins() {
        return userRepository.findAllByRole(UserRole.ROLE_ADMIN);
    }

    private void validateUniqueUsername(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ObjectConflictException(format("User with the same username '%s' already exists.", user.getUsername()));
        }
    }
}
