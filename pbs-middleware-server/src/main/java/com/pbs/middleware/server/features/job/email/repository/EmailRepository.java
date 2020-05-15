package com.pbs.middleware.server.features.job.email.repository;

import com.pbs.middleware.server.features.job.email.domain.EmailEntity;
import com.pbs.middleware.server.features.job.email.domain.EmailState;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends CrudRepository<EmailEntity, UUID> {

    Set<EmailEntity> findAllByState(EmailState state);

    @Modifying
    @Query("DELETE FROM EmailEntity e WHERE e.state = 'SEND' AND e.sendDate < :dateTime")
    void deleteOlderThan(@Param("dateTime") Long dateTime);

}
