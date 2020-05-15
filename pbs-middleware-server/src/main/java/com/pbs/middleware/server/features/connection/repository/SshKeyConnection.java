package com.pbs.middleware.server.features.connection.repository;

import com.pbs.middleware.server.features.connection.ConnectionConfig;
import com.pbs.middleware.server.features.connection.validations.SshKeyExists;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(ConnectionConfig.SSH_KEY_CONNECTION)
public class SshKeyConnection extends Connection {

    @NotBlank(message = "Connection ssh host is mandatory.")
    @Size(max = 100, message = "SSH host cannot be longer than 100 characters.")
    @Column(name = "ssh_host", length = 100)
    private String sshHost;

    @NotBlank(message = "Connection login is mandatory.")
    @Size(max = 50, message = "Login cannot be longer than 50 characters.")
    @Column(name = "login", length = 50)
    String login;

    @SshKeyExists
    @NotBlank(message = "Connection ssh kye is mandatory.")
    @Size(max = 300, message = "Path to SSH key cannot be longer than 300 characters.")
    @Column(name = "ssh_key", length = 300)
    String sshKey;

}
