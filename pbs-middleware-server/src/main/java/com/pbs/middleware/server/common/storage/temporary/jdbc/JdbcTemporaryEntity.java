package com.pbs.middleware.server.common.storage.temporary.jdbc;

import com.pbs.middleware.server.common.storage.temporary.TemporaryEntity;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(JdbcTemporaryEntityID.class)
@Table(
        name = "temporary_data",
        schema = "public",
        indexes = @Index(name = "IDX_TEMP", columnList = "id, domain_id")
)
public class JdbcTemporaryEntity implements TemporaryEntity {

    @Id
    private String id;

    @Id
    @Column(name = "domain_id")
    private UUID domainId;

    @Column(name = "size")
    private long size;

    @Column(name = "hash", columnDefinition = "TEXT")
    private String hash;

    @Column(name = "data", columnDefinition = "BLOB")
    private byte[] data;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public long getDataLength() {
        return getData() != null ? getData().length : 0;
    }
}
