package com.pbs.middleware.server.features.users.service;

import com.pbs.middleware.api.user.UserCreate;
import com.pbs.middleware.api.user.User;
import com.pbs.middleware.api.user.UserUpdate;
import com.pbs.middleware.server.features.users.repository.UserRole;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapToDto(com.pbs.middleware.server.features.users.repository.User user) {
        User dto = new User();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDisabled(user.isDisabled());
        dto.setRole(user.getRole().toString());

        return dto;
    }

    public com.pbs.middleware.server.features.users.repository.User mapFromDto(UserCreate dto) {
        com.pbs.middleware.server.features.users.repository.User user = new com.pbs.middleware.server.features.users.repository.User();

        user.setUsername(dto.getUsername());
        user.setRole(UserRole.valueOf(dto.getRole()));
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDisabled(false);

        return user;
    }

    public com.pbs.middleware.server.features.users.repository.User mapFromDto(UserUpdate dto) {
        return mapFromDto(null, dto);
    }

    public com.pbs.middleware.server.features.users.repository.User mapFromDto(UUID id, UserUpdate dto) {
        com.pbs.middleware.server.features.users.repository.User user = new com.pbs.middleware.server.features.users.repository.User();

        if (id != null) {
            user.setId(id);
        }
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDisabled(dto.getDisabled());

        return user;
    }

}
