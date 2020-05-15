package com.pbs.middleware.server.features.job.qstat;

import com.pbs.middleware.server.features.job.domain.JobState;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobQstatRepository extends JpaRepository<JobQstat, UUID> {

    @Modifying
    @Query("UPDATE JobQstat j SET j.qstat = :qstat WHERE j.id = :id")
    void updateState(@Param("qstat") String qstat, @Param("id") UUID id);

    @SuppressWarnings("all")
    @Query("SELECT new com.pbs.middleware.server.features.job.domain.JobState(j.id, j.status) FROM JobQstat j WHERE j.group = :group")
    Set<JobState> findByGroup(@Param("group") String group);

}
