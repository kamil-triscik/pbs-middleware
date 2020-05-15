package com.pbs.middleware.api.connection;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "CreateConnection", description = "Connection DTO for create request")
public class CreateConnection {

    @NotBlank(message = "Connection name cannot be empty.")
    @Size(max = 20, message = "Connection name cannot be longer than 20 characters.")
    @ApiModelProperty(name = "name", value = "Connection name")
    private String name;

    @Size(max = 100, message = "Connection description cannot be longer than 100 characters.")
    @ApiModelProperty(name = "description", value = "Connection description")
    private String description;

    @NotNull(message = "Connection type is mandatory.")
    @ApiModelProperty(name = "type", value = "Connection authentication type", allowableValues = "LOCAL, PASSWORD_AUTH, SSH_KEY_AUTH")
    private ConnectionType type;

    @NotBlank(message = "Pbs host cannot be empty.")
    @Size(max = 100, message = "Pbs host cannot be longer than 100 characters.")
    @ApiModelProperty(name = "pbsHost", value = "PBS host url")
    private String pbsHost;

    @Size(max = 100, message = "SSH host cannot be longer than 100 characters.")
    @ApiModelProperty(name = "sshHost", value = "SSH host url")
    private  String sshHost;

    @Size(max = 100, message = "Login cannot be longer than 100 characters.")
    @ApiModelProperty(name = "login", value = "SSH connection login")
    private String login;

    @Size(max = 100, message = "Password cannot be longer than 100 characters.")
    @ApiModelProperty(name = "password", value = "SSH connection user password")
    private String password;


    @Size(max = 300, message = "Path to SSH key cannot be longer than 300 characters.")
    @ApiModelProperty(name = "sshKey", value = "Path to private SSH key")
    private String sshKey;

}
