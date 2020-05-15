package com.pbs.middleware.server.common.storage.temporary.jdbc;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JdbcTemporaryEntityID implements Serializable {

    private String id;

    private UUID domainId;
}
