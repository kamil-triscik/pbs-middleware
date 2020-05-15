package com.pbs.middleware.server.features.connection.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, UUID> {

    @Query("SELECT c FROM Connection c WHERE c.id = :id OR c.name = :name")
    Optional<Connection> findByNameOrUuid(@Param("id") UUID id, @Param("name") String name);

    Optional<Connection> findByName(String name);

}
