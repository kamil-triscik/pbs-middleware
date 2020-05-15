package com.pbs.middleware.server.features.script.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptRepository extends CrudRepository<Script, UUID> {

    Optional<Script> findByName(String name);

    @Query("SELECT s FROM Script s WHERE s.id = :id OR s.name = :name")
    Optional<Script> findByNameOrUuid(@Param("id") UUID id, @Param("name") String name);


}
