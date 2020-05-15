package com.pbs.middleware.api.connection;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@ApiModel(value = "Connection", description = "Connection DTO for readonly purposes.")
public class Connection {

    @ApiModelProperty(name = "id", value = "Connection identification")
    private UUID id;

    @ApiModelProperty(name = "name", value = "Connection name")
    private String name;

    @ApiModelProperty(name = "description", value = "Connection description")
    private String description;

    @ApiModelProperty(name = "type", value = "Connection authentication type", allowableValues = "LOCAL, PASSWORD_AUTH, SSH_KEY_AUTH")
    private ConnectionType type;

    @ApiModelProperty(name = "pbsHost", value = "PBS host url")
    private String pbsHost;

    @ApiModelProperty(name = "sshHost", value = "SSH host url")
    private String sshHost;

    @ApiModelProperty(name = "login", value = "SSH connection login")
    private String login;

    @ApiModelProperty(name = "password", value = "SSH connection user password")
    private String password;

    @ApiModelProperty(name = "sshKey", value = "Path to private SSH key")
    private String sshKey;

}
