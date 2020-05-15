package com.pbs.middleware.server.features.users.repository;

import com.pbs.middleware.server.common.repository.EntityClass;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(
        name = "user",
        schema = "public",
        indexes = {
                @Index(name = "IDX_USER_EMAIL", columnList = "email"),
                @Index(name = "IDX_USER_NAME", columnList = "username"),
        }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends EntityClass {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole role;

    @Column(name = "is_disabled", nullable = false)
    private boolean disabled;
}
