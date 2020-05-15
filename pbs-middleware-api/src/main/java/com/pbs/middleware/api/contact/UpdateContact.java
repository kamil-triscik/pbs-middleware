package com.pbs.middleware.api.contact;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "UpdateContact", description = "Contact DTO for update request")
public class UpdateContact extends CreateContact {

}
