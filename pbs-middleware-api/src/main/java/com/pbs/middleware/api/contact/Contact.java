package com.pbs.middleware.api.contact;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "ContactDto", description = "Contact DTO for for readonly purposes")
public class Contact {

    @ApiModelProperty(name = "id", value = "Contact identification")
    private UUID id;

    @ApiModelProperty(name = "name", value = "Contact name")
    private String name;

    @ApiModelProperty(name = "email", value = "Contact email")
    private String email;

    @ApiModelProperty(name = "email", value = "Contact description")
    private String description;

    @ApiModelProperty(
            name = "contactTypes",
            value = "Contact types",
            allowableValues = "JOB, UPLOAD, DOWNLOAD, SUPPORT",
            example = "[UPLOAD, DOWNLOAD]")
    private Set<String> contactTypes;
}
