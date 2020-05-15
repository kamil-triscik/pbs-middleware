package com.pbs.middleware.server.features.ownership.repository;

import com.pbs.middleware.server.features.ownership.domain.DomainType;
import com.pbs.middleware.server.features.ownership.domain.Ownership;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnershipRepository extends JpaRepository<Ownership, UUID> {

    /**
     * This method return ids of all objects with specified domain type for specified owner.
     *
     * @param owner id of owner/user
     * @param type domain type of objects.
     * @return collection of ids
     */
    @SuppressWarnings("all")
    @Query("SELECT o.domain AS id FROM Ownership o WHERE o.owner = :owner AND o.domainType = :type")
    Set<UUID> findAllDByOwnerAndDomainType(@Param("owner") UUID owner, @Param("type") DomainType type);

    /**
     * This method return ids of all objects with specified domain type.
     *
     * @param type domain type of objects.
     * @return collection of ids
     */
    @SuppressWarnings("all")
    @Query("SELECT o.domain AS id FROM Ownership o WHERE o.domainType = :type")
    Set<UUID> findAllByDomainType(DomainType type);

    boolean existsOwnershipByOwnerAndAndDomain(UUID owner, UUID domain);

    boolean existsByDomain(UUID domain);

    void deleteByDomain(UUID domainId);

}
