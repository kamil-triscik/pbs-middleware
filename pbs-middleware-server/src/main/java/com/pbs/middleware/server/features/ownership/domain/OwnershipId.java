package com.pbs.middleware.server.features.ownership.domain;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnershipId implements Serializable {

    private UUID domain;

    private UUID owner;

}
