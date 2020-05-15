package com.pbs.middleware.server.features.users.rest;

import com.pbs.middleware.api.user.ApiConfig;
import com.pbs.middleware.api.user.UserCreate;
import com.pbs.middleware.api.user.User;
import com.pbs.middleware.api.user.UserPasswordUpdate;
import com.pbs.middleware.api.user.UserUpdate;
import com.pbs.middleware.server.features.documentation.TagsConfig;
import com.pbs.middleware.server.features.users.service.UserMapper;
import com.pbs.middleware.server.features.users.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.security.Principal;
import java.util.UUID;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import static com.pbs.middleware.server.common.security.SecurityConfig.ALLOW_ADMIN;
import static com.pbs.middleware.server.common.security.SecurityConfig.ALLOW_AUTHENTICATED;
import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping(ApiConfig.USERS)
@RequiredArgsConstructor
@Api(tags = TagsConfig.USERS_TAG)
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @NonNull
    private final UserService service;

    @NonNull
    private final UserMapper mapper;

    @PreAuthorize(ALLOW_ADMIN)
    @ApiOperation(value = "Retrieves all Users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All users loaded"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(ApiConfig.USERS_GET_ALL)
    public Page<User> getAll(@PageableDefault(sort = {"username"}) @ApiIgnore Pageable pageable) {
        return service.getAll(pageable).map(mapper::mapToDto);
    }

    @ApiOperation(value = "Retrieves one user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User object"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize(ALLOW_ADMIN)
    @GetMapping(ApiConfig.USERS_GET)
    public User get(@PathVariable UUID id) {
        return mapper.mapToDto(service.get(id));
    }

    @ApiOperation(value = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "New user created!"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 409, message = "User already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize(ALLOW_ADMIN)
    @PostMapping
    public User create(@Valid @RequestBody UserCreate userDto) {
        requireNonNull(userDto);
        return mapper.mapToDto(service.create(mapper.mapFromDto(userDto)));
    }

    @ApiOperation(value = "Reset user password")
    @PreAuthorize(ALLOW_ADMIN)
    @PostMapping(ApiConfig.USERS_RESET_PW)
    public void resetPassword(@PathVariable UUID id) {
        requireNonNull(id);
        service.resetPassword(id);
    }

    @ApiOperation(value = "Disable user")
    @PreAuthorize(ALLOW_ADMIN)
    @PostMapping(ApiConfig.USERS_DISABLE)
    public void disable(@PathVariable UUID id) {
        requireNonNull(id);
        service.enableDisableUser(id, true);
    }

    @ApiOperation(value = "Enable user")
    @PreAuthorize(ALLOW_ADMIN)
    @PostMapping(ApiConfig.USERS_ENABLE)
    public void enable(@PathVariable UUID id) {
        requireNonNull(id);
        service.enableDisableUser(id, false);
    }

    @ApiOperation(value = "Update user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User updated!"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize(ALLOW_ADMIN)
    @PutMapping(ApiConfig.USERS_UPDATE)
    public User update(@PathVariable UUID id, @Valid @RequestBody UserUpdate userUpdateDto) {
        requireNonNull(userUpdateDto);
        return mapper.mapToDto(service.update(id, mapper.mapFromDto(userUpdateDto)));
    }

    /**
     * Returns currently logged in user based on {@link java.security.Principal}. This method is usually called from UI.
     *
     * @param principal principal is auto-injected by Spring Security
     * @return currently logged in user
     */
    @PreAuthorize(ALLOW_AUTHENTICATED)
    @GetMapping(ApiConfig.USERS_ME)
    public User me(Principal principal) {
        return mapper.mapToDto(service.getByUsername(principal.getName()));
    }

    /**
     * Returns currently logged in user. This method is used only by form login authentication. The current user
     * is detected from {@link org.springframework.security.core.Authentication} of the current {@link org.springframework.security.core.context.SecurityContextHolder#getContext()}.
     * <p>
     * Spring Security Form-Login implementation forwards the original POST request to this method.
     *
     * @return currently logged in user
     * see com.pbs.middleware.server.common.security.SecurityConfig#webSecurityConfigurerAdapter(SecurityUserDetailsService, SecurityProperties)
     * see SecurityProperties#getLoginSuccessUrl()
     */
    @PreAuthorize(ALLOW_AUTHENTICATED)
    @PostMapping(ApiConfig.USERS_ME)
    public User me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return mapper.mapToDto(service.getByUsername(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername()));
    }

    /**
     * Spring Security forwards a POST request to this method on failed login.
     *
     * @return 403 {@link org.springframework.http.HttpStatus#FORBIDDEN}
     */
    @PostMapping(ApiConfig.USERS_LOGIN_FAILED)
    public ResponseEntity<Void> loginFailed() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    /**
     * Changes password for user by given {@code id}. Both old and new passwords must be provided.
     *
     * @param id                user id
     * @param passwordUpdateDto contains old and new passwords
     * @throws org.springframework.security.authentication.BadCredentialsException if old password does not match current password, or user not found
     */
    @PreAuthorize(ALLOW_AUTHENTICATED)
    @PutMapping(value = ApiConfig.USERS_CHANGE_PW, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changePassword(@PathVariable UUID id, @Valid @RequestBody UserPasswordUpdate passwordUpdateDto) {
        service.changePassword(id, passwordUpdateDto.getOldPassword(), passwordUpdateDto.getNewPassword());
    }

}
