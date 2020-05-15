package com.pbs.middleware.server.features.ownership.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "ownership",
        schema = "public",
        indexes = @Index(name = "IDX_OWNERSHIP", columnList = "domain_id, owner_id")
)
@Data
@IdClass(OwnershipId.class)
@AllArgsConstructor
@NoArgsConstructor
public class Ownership {

    @Id
    @Column(name = "domain_id", nullable = false, unique = true)
    private UUID domain;

    @Id
    @Column(name = "owner_id", nullable = false)
    private UUID owner;

    @Enumerated(EnumType.STRING)
    @Column(name = "domain_type", nullable = false)
    private DomainType domainType;
}
