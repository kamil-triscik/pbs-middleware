package com.pbs.middleware.api.connection;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ConnectionUpdate", description = "Connection DTO for update request")
public class UpdateConnection extends CreateConnection {

}
